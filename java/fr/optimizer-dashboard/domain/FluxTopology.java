package fr.ifpen.synergreen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ifpen.synergreen.domain.enumeration.DataType;
import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A FluxTopology.
 */
@Entity
@Table(name = "flux_topology")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fluxtopology")
public class FluxTopology implements Serializable, FluxNodeInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "fluxTopology")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 5)
    private List<FluxNode> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "energy_provider_id")
    private EnergyProvider energyProvider;

    @ManyToOne
    @JoinColumn(name = "energy_site_id")
    private EnergySite energySite;

    @Column(name = "optimization")
    private boolean isOptimization;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "parentTopology")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FluxTopology> optimizations = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FluxTopology parentTopology;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "fluxTopology")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 5)
    private List<Invoice> invoices = new ArrayList<>();

    @Column(name = "img")//fixme
    private String img; //carte

    @OneToOne(mappedBy = "fluxTopology", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BatteryManager batteryManager;  // Gestionnaire de run pour l'algo de battery-management de batterie

    @Transient
    private StateFluxNodeDTO stateFluxNodeDTO = new StateFluxNodeDTO();

    /*getters and setters*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FluxNode> getChildren() {
        return children;
    }

    public void setChildren(List<FluxNode> children) {
        children.forEach(c -> c.setFluxTopology(this));
        this.children = children;
    }

    public FluxNode findOneChild(Long fluxNodeId) {
        List<FluxNode> children = this.getChildren();
        for (FluxNode fluxNode : children) {
            if (fluxNode.getId().equals(fluxNodeId)) {
                return fluxNode;
            }
        }
        return null;
    }

    public EnergyProvider getEnergyProvider() {

        return energyProvider;
    }

    public void setEnergyProvider(EnergyProvider energyProvider) {

        this.energyProvider = energyProvider;
    }

    public EnergySite getEnergySite() {
        return energySite;
    }

    public void setEnergySite(EnergySite energySite) {
        this.energySite = energySite;
    }

    public FluxTopology energySite(EnergySite energySite) {
        this.energySite = energySite;
        return this;
    }

    public boolean isOptimization() {

        return isOptimization;
    }

    public void setOptimization(boolean optimization) {

        isOptimization = optimization;
    }

    public FluxTopology getParentTopology() {
        return parentTopology;
    }

    public void setParentTopology(FluxTopology parentTopology) {
        this.parentTopology = parentTopology;
    }

    public Set<FluxTopology> getOptimizations() {
        return optimizations;
    }

    public void setOptimizations(Set<FluxTopology> optimizations) {
        this.optimizations = optimizations;
    }

    public StateFluxNodeDTO getStateFluxNodeDTO() {
        return stateFluxNodeDTO;
    }

    public void setStateFluxNodeDTO(StateFluxNodeDTO stateFluxNodeDTO) {
        this.stateFluxNodeDTO = stateFluxNodeDTO;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BatteryManager getBatteryManager() {
        return batteryManager;
    }

    public void setBatteryManager(BatteryManager batteryManager) {
        this.batteryManager = batteryManager;
    }

    /*methods */
    public List<StateFluxNodeDTO> getStateSummary(FluxPeriod fP) {
        List<StateFluxNodeDTO> stateTopology = new ArrayList<>(); //List of state nodes (elements and groups) that belong to a topology
        List<FluxNode> nodes = new ArrayList<>();//all nodes in a topology (elements and groups)

        /*construction of state for a topology*/
        stateFluxNodeDTO.setName(getName());
        stateFluxNodeDTO.setType(FluxNodeType.TOPOLOGY);
        stateFluxNodeDTO.setId(getId());

        /*In order to get all state nodes (balancing) from all children (groups ans eelements) we use "getStateSummaryNode" */
        children.stream()
            .map(c -> c.getStateSummaryNode(fP))
            .collect(Collectors.toList());

        /*Finally we need feed the state topology so we call "calculate state" and we pass teh children from the first level (without children from children)*/
        calculateState(children.stream().map(c -> c.getStateFluxNodeDTO()).collect(Collectors.toList()));

      /*Now, after of balancing and getting states from all nodes and including the topology parent we goint to calculate the energy and the related invoice */

        /*Get all fluxNodes from a topology in order to get the energyProvider and to calculate the energy*/
        nodes.addAll(getChildren().stream()
            .map(FluxNode::getAllChildren)
            .flatMap(Collection::stream)
            .collect(Collectors.toList()));

         /*Calcul d'energie pour all elements and groups children*/
        nodes.forEach(c -> {
            EnergyProvider provider = c.findProvider();
            provider.buildMatrixPrices();
            c.getStateFluxNodeDTO().setPriceCons(auxPriceCon(c.getStateFluxNodeDTO().getpConsoFromGrid(), provider, c.getId(), DataType.CONSOMMATION_FROM_GRID));
            c.getStateFluxNodeDTO().getPriceCons().put("EnergyTotal", computeEnergy(c.getStateFluxNodeDTO().getpConsoGlobal(), c.getId(), DataType.CONSOMMATION).stream()
                .mapToDouble(x -> x.getMeasure()).sum());

            Double cT = stateFluxNodeDTO.getpConsoGlobal().stream().mapToDouble(MeasuredData::getMeasure).sum();
            c.getStateFluxNodeDTO().getPriceCons().put("SelfCons",
                (cT > 0 ?
                    ((c.getStateFluxNodeDTO().getpConsoFromBat().stream().mapToDouble(MeasuredData::getMeasure).sum() +
                        c.getStateFluxNodeDTO().getpConsoFromProd().stream().mapToDouble(MeasuredData::getMeasure).sum()) /
                        cT) * 100 : 0.));

            c.getStateFluxNodeDTO().setPriceProdSell(auxPriceProdSell(c.getStateFluxNodeDTO().getpProdSentToGrid(), provider, c.getId(), DataType.PRODUCTION_TO_SELL));
            c.getStateFluxNodeDTO().getPriceProdSell().put("EnergyTotal", computeEnergy(c.getStateFluxNodeDTO().getpProdGlobal(), c.getId(), DataType.PRODUCTION).stream()
                .mapToDouble(x -> x.getMeasure()).sum());

            Double pT = stateFluxNodeDTO.getpProdGlobal().stream().mapToDouble(MeasuredData::getMeasure).sum();
            c.getStateFluxNodeDTO().getPriceProdSell().put("SelfProd",
                (pT > 0 ?
                    ((c.getStateFluxNodeDTO().getpProdConsByConsumers().stream().mapToDouble(MeasuredData::getMeasure).sum() +
                        c.getStateFluxNodeDTO().getpProdConsByBat().stream().mapToDouble(MeasuredData::getMeasure).sum()) /
                        pT) * 100 : 0.));
        });

        /*Here, we put or save in the state topology the priceCons and priceSell*/
        stateFluxNodeDTO.setPriceCons(prixByType(children.stream().map(c -> c.getStateFluxNodeDTO()).collect(Collectors.toList()), DataType.PRIX_CONS));
        stateFluxNodeDTO.setPriceProdSell(prixByType(children.stream().map(c -> c.getStateFluxNodeDTO()).collect(Collectors.toList()), DataType.PRIX_PROD_SELL));

        /*Ajoute l'etat de la topology dans la list qui sera envoyé à l'interface*/
        stateTopology.add(stateFluxNodeDTO);
        /*Recupere l'etat de tous ses descendents : enfants et leurs enfants jusqu'à le dernier descendent */
        stateTopology.addAll(
            getChildren()
                .stream()
                .map(
                    FluxNode::getAllStateChildren
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));


/*        stateTopology.stream().forEach(c -> {
            try {
                File testFile = new File(c.getId() + "_" + c.getName() + ".csv");
                BufferedWriter out = new BufferedWriter(new FileWriter(testFile));

                out.write("EnergyTotal;EnergyFromGrid;SelfCons;Hp;Hc\n");
                out.write(
                    c.getPriceCons().get("EnergyTotal").toString() + ";" +
                        c.getPriceCons().get("EnergyGrid").toString() + ";" +
                        c.getPriceCons().get("SelfCons").toString() + ";" +
                        c.getPriceCons().get("HP").toString() + ";" +
                        c.getPriceCons().get("HC").toString() + "\n");
                out.write("EnergyTotal;EnergyToSell;SelfProd;Cost\n");
                out.write(
                    c.getPriceProdSell().get("EnergyTotal").toString() + ";" +
                        c.getPriceProdSell().get("EnergySell").toString() + ";" +
                        c.getPriceProdSell().get("SelfProd").toString() + ";" +
                        c.getPriceProdSell().get("Cost").toString() + "\n");

                out.write("Date;PconsoGlobal;PconsoFromProd;PconsoFromGrid;PconsoFromBat;PprodGlobal;PprodSentToGrid;PprodConsByConsumers;PprodConsByBat\n");

                for (int i = 0; i < c.getpProdGlobal().size(); i++) {
                    out.write(
                        c.getpConsoGlobal().get(i).getInstant() + ";" +
                            c.getpConsoGlobal().get(i).getMeasure() + ";" +
                            c.getpConsoFromProd().get(i).getMeasure() + ";" +
                            c.getpConsoFromGrid().get(i).getMeasure() + ";" +
                            c.getpConsoFromBat().get(i).getMeasure() + ";" +
                            c.getpProdGlobal().get(i).getMeasure() + ";" +
                            c.getpProdSentToGrid().get(i).getMeasure() + ";" +
                            c.getpProdConsByConsumers().get(i).getMeasure() + ";" +
                            c.getpProdConsByBat().get(i).getMeasure() + "\n");
                    out.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });*/

        return stateTopology;
    }

    /*Methode qui permet calculer l'etat de la topologie, en considerant l'etat de ses enfants,
     * ici il faut juste faire une sommation des etats des enfants*/
    public void calculateState(List<StateFluxNodeDTO> stateFluxNodeList) {
        stateFluxNodeDTO.setpProdGlobal(filterByType(stateFluxNodeList, DataType.PRODUCTION));
        stateFluxNodeDTO.setpProdSentToGrid(filterByType(stateFluxNodeList, DataType.PRODUCTION_TO_SELL));
        stateFluxNodeDTO.setpProdConsByBat(filterByType(stateFluxNodeList, DataType.PRODUCTION_CONSUMED_BY_BATTERIES));
        stateFluxNodeDTO.setpProdConsByConsumers(filterByType(stateFluxNodeList, DataType.PRODUCTION_SELF_CONSUMED));

        stateFluxNodeDTO.setpConsoGlobal(filterByType(stateFluxNodeList, DataType.CONSOMMATION));
        stateFluxNodeDTO.setpConsoFromGrid(filterByType(stateFluxNodeList, DataType.CONSOMMATION_FROM_GRID));
        stateFluxNodeDTO.setpConsoFromProd(filterByType(stateFluxNodeList, DataType.CONSOMMATION_FROM_PRODUCTION));
        stateFluxNodeDTO.setpConsoFromBat(filterByType(stateFluxNodeList, DataType.CONSOMMATION_FROM_STORAGE));

        stateFluxNodeDTO.setpBatGlobal(filterByType(stateFluxNodeList, DataType.STORAGE));
        stateFluxNodeDTO.setpBConsoFromGrid(filterByType(stateFluxNodeList, DataType.STORAGE_CONSOMMATION_FROM_GRID));
        stateFluxNodeDTO.setpBatGlobal(filterByType(stateFluxNodeList, DataType.STORAGE_PRODUCTION_CONSUMED_BY_CONSUMERS));
    }

    /*Methode auxilier qui permet faire la sommation de prix d'energie pour la topologie*/
    private Map<String, Double> prixByType(List<StateFluxNodeDTO> node, DataType type) {
        Map<String, Double> prix = new HashMap<>();
        Double prixHeurC = 0.;
        Double prixHeurP = 0.;
        Double prixHeure = 0.;
        Double energyTotal = 0.;
        Double energy = 0.;
        Double self = 0.;

        switch (type) {
            case PRIX_CONS:
                prixHeurC = node.stream()
                    .mapToDouble(c -> c.getPriceCons().get("HC")).sum();
                prixHeurP = node.stream()
                    .mapToDouble(c -> c.getPriceCons().get("HP")).sum();
                energyTotal = node.stream()
                    .mapToDouble(c -> c.getPriceCons().get("EnergyTotal")).sum();
                energy = node.stream()
                    .mapToDouble(c -> c.getPriceCons().get("EnergyGrid")).sum();
                self = node.stream().mapToDouble(c -> c.getPriceCons().get("SelfCons")).sum();

                prix.put("EnergyGrid", energy);
                prix.put("SelfCons", self);
                prix.put("EnergyTotal", energyTotal);
                prix.put("HC", prixHeurC);
                prix.put("HP", prixHeurP);
                break;
            case PRIX_PROD_SELL:
                prixHeure = node.stream()
                    .mapToDouble(c -> c.getPriceProdSell().get("Cost")).sum();
                energyTotal = node.stream()
                    .mapToDouble(c -> c.getPriceProdSell().get("EnergyTotal")).sum();
                energy = node.stream()
                    .mapToDouble(c -> c.getPriceProdSell().get("EnergySell")).sum();
                self = node.stream().mapToDouble(c -> c.getPriceProdSell().get("SelfProd")).sum();

                prix.put("EnergySell", energy);
                prix.put("SelfProd", self);
                prix.put("EnergyTotal", energyTotal);
                prix.put("Cost", prixHeure);
                break;
            default:
                break;
        }
        return prix;
    }

    /*Methode auxilier pour faire la sommation de puissances selon le type: puissance consumee de la reseau..;*/
    private List<MeasuredData> filterByType(List<StateFluxNodeDTO> node, DataType type) {
        List<MeasuredData> filteredList;
        switch (type) {
            case CONSOMMATION:
                filteredList = node.stream()
                    .map(s -> s.getpConsoGlobal())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList());
                break;
            case CONSOMMATION_FROM_GRID:
                filteredList = node.stream()
                    .map(s -> s.getpConsoFromGrid())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList());
                break;
            case CONSOMMATION_FROM_PRODUCTION:
                filteredList = node.stream()
                    .map(s -> s.getpConsoFromProd())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;
            case CONSOMMATION_FROM_STORAGE:
                filteredList = node.stream()
                    .map(s -> s.getpConsoFromBat())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;

            case PRODUCTION:
                filteredList = node.stream()
                    .map(s -> s.getpProdGlobal())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;
            case PRODUCTION_TO_SELL:
                filteredList = node.stream()
                    .map(s -> s.getpProdSentToGrid())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;
            case PRODUCTION_SELF_CONSUMED:
                filteredList = node.stream()
                    .map(s -> s.getpProdConsByConsumers())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;
            case PRODUCTION_CONSUMED_BY_BATTERIES:
                filteredList = node.stream()
                    .map(s -> s.getpProdConsByBat())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;

            case STORAGE:
                filteredList = node.stream()
                    .map(s -> s.getpBatGlobal())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;
            case STORAGE_CONSOMMATION_FROM_GRID:
                filteredList = node.stream()
                    .map(s -> s.getpBConsoFromGrid())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;
            default:
                filteredList = node.stream()
                    .map(s -> s.getpBProdConsByConsumers())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList()); //fixme reviser calcul sum consummations
                break;
        }
        return filteredList;
    }

    /*Calculate prix d'energy*/
    public Map<String, Double> auxPriceCon(List<MeasuredData> list, EnergyProvider e, Long id, DataType dataType) {
        Map<String, Double> prixCons = new HashMap<>();
        Double prixHeurC = 0.;
        Double prixHeurP = 0.;
        List<MeasuredData> energyList = computeEnergy(list, id, dataType);
        Integer idx = 1;


       /* try {
            File nodeFile = new File("Energy_Price_Con_" + id + "_" + dataType + ".csv");
            BufferedWriter bufferOut = new BufferedWriter(new FileWriter(nodeFile));
            bufferOut.write("Type;Time;vEnergy;PurchasePrice;isHigh\n");

*/
        for (MeasuredData x : energyList) { //heurs crouse /plein

            Double purchasePrice = getPriceEnergy(e, list.get(idx - 1).getInstant());  //prix fournisseur vente, le prix avec lequel on achete à fournisseur

            if (e.getHighHours().contains(list.get(idx - 1).getInstant().getHour())
                || e.getPeakHours().contains(list.get(idx - 1).getInstant().getHour())) {

                prixHeurP += x.getMeasure() * purchasePrice;
                    /*bufferOut.write(dataType + ";" + list.get(idx - 1).getInstant() + ";" +
                        x.getMeasure() + ";" +
                        purchasePrice + ";" +

                        "true\n");*/

            } else if (e.getLowHours().contains(list.get(idx - 1).getInstant().getHour())) {
                prixHeurC += x.getMeasure() * purchasePrice;
                   /* bufferOut.write(dataType + ";" + list.get(idx - 1).getInstant() + ";" +
                        x.getMeasure() + ";" +
                        purchasePrice + ";" +

                        "false\n");*/
            }

            idx++;
        }

           /* bufferOut.flush();
        } catch (IOException f) {
            f.printStackTrace();

        }*/

        prixCons.put("EnergyGrid", energyList.stream().mapToDouble(x -> x.getMeasure()).sum());
        prixCons.put("HC", prixHeurC);
        prixCons.put("HP", prixHeurP);

        return prixCons;
    }

    /*Calcul de prix apartir de l'energie*/
    public Map<String, Double> auxPriceProdSell(List<MeasuredData> list, EnergyProvider e, Long id, DataType dataType) {
        Map<String, Double> prixProd = new HashMap<>();
        Double prixHeure = 0.;
        List<MeasuredData> energyList = computeEnergy(list, id, dataType);

       /* try {
            File nodeFile = new File("Energy_Price_Prod_" + id + "_" + dataType + ".csv");
            BufferedWriter bufferOut = new BufferedWriter(new FileWriter(nodeFile));
            bufferOut.write("Type;Time;vEnergy;PurchasePrice\n");*/

        for (MeasuredData x : energyList) {
               /* bufferOut.write(dataType + ";" + x.getInstant() + ";" +
                    x.getMeasure() + ";" +
                    e.getPurchaseRate() + "\n");*/
            prixHeure += x.getMeasure() * e.getPurchaseRate();
        }

           /* bufferOut.flush();
        } catch (IOException f) {
            f.printStackTrace();

        }*/

        prixProd.put("EnergySell", energyList.stream().mapToDouble(x -> x.getMeasure()).sum());
        prixProd.put("Cost", prixHeure);

        return prixProd;
    }

    /*energy*/
    public List<MeasuredData> computeEnergy(List<MeasuredData> list, Long id, DataType dataType) {

        if (!list.isEmpty()) {
            List<MeasuredData> energies = new ArrayList<>();
            Iterator<MeasuredData> it = list.iterator();
            Iterator<MeasuredData> itnx = list.iterator();
            itnx.next();


           /* try {
                File nodeFile = new File("Energy_" + id + "_" + dataType + ".csv");
                BufferedWriter bufferOut = new BufferedWriter(new FileWriter(nodeFile));
                bufferOut.write("Type;TimeA;TimeB;PowerA;PowerB;Base;Energy\n");*/

            while (itnx.hasNext()) {
                MeasuredData e = it.next();
                MeasuredData enx = itnx.next();
                ZonedDateTime t = e.getInstant();
                ZonedDateTime tnx = enx.getInstant();

                //consumed energy from provider
                Double mc = e.getMeasure(); //puissance current
                Double mnx = enx.getMeasure();
                Long bc = tnx.toEpochSecond() - t.toEpochSecond(); //base

                Double ec = (((mc + mnx) / 2.0) * bc); // Integrate
                ec = ec / 3600.; // Set to KWh ; echelles qui changent
                energies.add(new MeasuredData(tnx, ec, true, DataType.ENERGY));

                    /*bufferOut.write(dataType + ";" + t + ";" +

                        tnx + ";" + mc + ";" + mnx + ";" + bc + ";" + ec + "\n");*/
            }

               /* bufferOut.flush();
            } catch (IOException f) {
                f.printStackTrace();

            }*/
            return energies;

        } else {
            return Collections.emptyList();
        }
    }


    /*fixme  maybe we can move to provider class*/
    public Double getPriceEnergy(EnergyProvider e, ZonedDateTime t) {

        Integer id = 0;
        if (e.getMonthsHighSeasons().contains(t.getMonth()) && e.getLowHours().contains(t.getHour()))
            id = id + 4;
        if (e.getMonthsHighSeasons().contains(t.getMonth()) && e.getHighHours().contains(t.getHour()))
            id = id + 3;
        if (e.getMonthsHighSeasons().contains(t.getMonth()) && e.getPeakHours().contains(t.getHour()))
            // Not taking into account peak hours just now. As such, peak hours are to be considered as high hours.
            id = id + 3;
        if (!e.getMonthsHighSeasons().contains(t.getMonth()) && e.getLowHours().contains(t.getHour()))
            id = id + 1;
        if (!e.getMonthsHighSeasons().contains(t.getMonth()) && e.getHighHours().contains(t.getHour()))
            id = id + 0;
        if (!e.getMonthsHighSeasons().contains(t.getMonth()) && e.getPeakHours().contains(t.getHour()))
            // Not taking into account peak hours just now. As such, peak hours are to be considered as high hours.
            id = id + 0;

        return e.getSellPriceMatrix().get(id);

    }

}
