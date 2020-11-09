package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.domain.enumeration.DataType;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A FluxGroup.
 */
@Entity
@DiscriminatorValue("FluxGroup")
public class FluxGroup extends FluxNode implements Serializable, FluxNodeInterface {

    @OneToMany(mappedBy = "parent")
    private Set<FluxNode> children = new HashSet<>();

    /*getters and setters*/
    public Set<FluxNode> getChildren() {
        return children;
    }

    public void setChildren(Set<FluxNode> children) {
        this.children = children;
    }

    /*methods*/

    /*Here we can get all state nodes (groups ans elements) that belong to this group*/
    public List<StateFluxNodeDTO> getAllStateChildren() {
        List<StateFluxNodeDTO> states = new ArrayList<>();
        states.add(getStateFluxNodeDTO());

        states.addAll(
            getChildren()
                .stream()
                .map(FluxNode::getAllStateChildren)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return states;
    }

    /*Here we can get all nodes (groups ans elements) that belong to this group*/
    public List<FluxNode> getAllChildren() {
        List<FluxNode> nodes = new ArrayList<>();
        nodes.add(this); //add group node
        nodes.addAll(
            getChildren()
                .stream()
                .map(FluxNode::getAllChildren)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return nodes;
    }

    /*In this method we calculate the state for this group, but first we need calculate the same method in all children and after we make an balancing state*/
    public StateFluxNodeDTO getStateSummaryNode(FluxPeriod fP) {
        stateFluxNodeDTO.setName(getName());
        stateFluxNodeDTO.setId(getId());
        stateFluxNodeDTO.setType(getType());

        calculateState(
            children.stream()
                .map(c -> c.getStateSummaryNode(fP)) //launch the calculate of state in all children
                .collect(Collectors.toList()));

        return stateFluxNodeDTO;
    }

    /*Here we use state from children in order to summarize the state for this group, we need too apply an algorithm de balancing node*/
    public void calculateState(List<StateFluxNodeDTO> stateFluxNodeList) {
        stateFluxNodeDTO.setdCons(filterByType(stateFluxNodeList, DataType.D_CONS));
        stateFluxNodeDTO.setdProd(filterByType(stateFluxNodeList, DataType.D_PROD));
        stateFluxNodeDTO.setdStrg(filterByType(stateFluxNodeList, DataType.D_STRG));

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

        Integer i = 0;
        while (i < stateFluxNodeDTO.getdProd().size()) {
            balancingState(i, 0., 0., 0., 0., 0., 0., 0., 0.);
            i++;
        }
    }

    /*Algorithm for balancing nodes*/
    public void balancingState(Integer i, Double fConsoFromGrid, Double fConsoFromProd, Double fConsoFromBat, Double fProdConsByBat, Double fProdConsByConsumers, Double fProdSentToGrid, Double fBatConsFromGrid, Double fBatProdConsByCons) {
        Double consoGlobal, consoFromBat, consoFromGrid, consoFromProd,
            prodGlobal, prodConsByBat, prodConsByConsumers, prodSentToGrid,
            strgGlobal, batConsFromGrid,
            batProdConsByCons,
            diff,
            dProdGlobal, dConsoGlobal, dStrgGlobal,
            dProdChildren, dConsChildren, dStrgChildren;  //dProd et dCons permettent connaitre quel est la disponibilté de consommation ou production du node

        prodGlobal = (stateFluxNodeDTO.getpProdGlobal().size() > 0 ? stateFluxNodeDTO.getpProdGlobal().get(i).getMeasure() : 0);
        consoGlobal = (stateFluxNodeDTO.getpConsoGlobal().size() > 0 ? stateFluxNodeDTO.getpConsoGlobal().get(i).getMeasure() : 0);
        strgGlobal = (stateFluxNodeDTO.getpBatGlobal().size() > 0 ? stateFluxNodeDTO.getpBatGlobal().get(i).getMeasure() : 0);
        dProdGlobal = (stateFluxNodeDTO.getdProd().size() > 0 ? stateFluxNodeDTO.getdProd().get(i).getMeasure() : 0);
        dConsoGlobal = (stateFluxNodeDTO.getdCons().size() > 0 ? stateFluxNodeDTO.getdCons().get(i).getMeasure() : 0);
        dStrgGlobal = (stateFluxNodeDTO.getdStrg().size() > 0 ? stateFluxNodeDTO.getdStrg().get(i).getMeasure() : 0);
        dConsChildren = dConsoGlobal;
        dProdChildren = dProdGlobal;
        dStrgChildren = dStrgGlobal;

        if (fluxTopology == null) { //it's not a border node

            if (dConsoGlobal != 0 && dProdGlobal != 0) {  //sans considerer la batterie
                diff = dProdGlobal - dConsoGlobal;

                if (diff < 0) {
                    consoFromGrid = 0.;
                    prodSentToGrid = 0.;

                    consoFromProd = dProdGlobal; //ne considere pas la production de la battery
                    consoFromBat = (dStrgGlobal > 0 ? 0. : Math.abs(dStrgGlobal)); //>0 batt consume power ; <0 batt produit power
                    prodConsByConsumers = dProdGlobal; //consFromProd
                    prodConsByBat = (dStrgGlobal <= 0 ? 0. : dConsoGlobal >= dStrgGlobal ? 0. : (dStrgGlobal - dConsoGlobal));
                    batConsFromGrid = (dStrgGlobal <= 0 ? 0. : dStrgGlobal - dProdGlobal);
                    batProdConsByCons = consoFromBat;

                } else if (diff > 0) {
                    consoFromGrid = 0.;
                    prodSentToGrid = 0.;

                    consoFromProd = dConsoGlobal + (dStrgGlobal > 0 ? dStrgGlobal : 0.); //en considerant battery
                    consoFromBat = (dStrgGlobal > 0 ? 0. : dProdGlobal > dConsoGlobal ? 0. : (dConsoGlobal - dProdGlobal)); //>0 batt consume power ; <0 batt produit power
                    prodConsByConsumers = dConsoGlobal;
                    prodConsByBat = (dStrgGlobal < 0 ? 0. : dStrgGlobal);
                    batConsFromGrid = 0.;
                    batProdConsByCons = consoFromBat;
                } else {
                    consoFromGrid = 0.;
                    consoFromProd = dConsoGlobal + (dStrgGlobal > 0 ? dStrgGlobal : 0.); //en considerant la production de la battery aussi
                    consoFromBat = (dStrgGlobal > 0 ? 0. : dStrgGlobal); //>0 batt consume power ; <0 batt produit power
                    prodConsByConsumers = dProdGlobal;
                    prodConsByBat = (dStrgGlobal < 0 ? 0. : dStrgGlobal);
                    prodSentToGrid = 0.;
                    batConsFromGrid = 0.;
                    batProdConsByCons = consoFromBat;
                }

                dProdGlobal = prodGlobal - (prodConsByConsumers + prodConsByBat + prodSentToGrid);
                dConsoGlobal = consoGlobal - (consoFromBat + consoFromGrid + consoFromProd);
                dStrgGlobal = 0.;

                //add facteurs de correction de consommation et production
                stateFluxNodeDTO.getdCons().get(i).setMeasure(dConsoGlobal);
                stateFluxNodeDTO.getdProd().get(i).setMeasure(dProdGlobal);
                stateFluxNodeDTO.getpConsoFromGrid().get(i).setMeasure(consoFromGrid);
                stateFluxNodeDTO.getpConsoFromProd().get(i).setMeasure(consoFromProd);
                stateFluxNodeDTO.getpConsoFromBat().get(i).setMeasure(consoFromBat);
                stateFluxNodeDTO.getpProdSentToGrid().get(i).setMeasure(prodSentToGrid);
                stateFluxNodeDTO.getpProdConsByConsumers().get(i).setMeasure(prodConsByConsumers);
                stateFluxNodeDTO.getpProdConsByBat().get(i).setMeasure(prodConsByBat);

                //balancing children
                fConsoFromGrid = (dConsChildren != 0 ? consoFromGrid / dConsChildren : 0.);
                fConsoFromBat = (dConsChildren != 0 ? consoFromBat / dConsChildren : 0.);
                fConsoFromProd = (dConsChildren != 0 ? consoFromProd / dConsChildren : 0.);
                fProdConsByBat = (dProdChildren != 0 ? prodConsByBat / dProdChildren : 0.);
                fProdConsByConsumers = (dProdChildren != 0 ? prodConsByConsumers / dProdChildren : 0.);
                fProdSentToGrid = (dProdChildren != 0 ? prodSentToGrid / dProdChildren : 0.);
                fBatConsFromGrid = (dStrgChildren != 0 ? batConsFromGrid / dStrgChildren : 0.);
                fBatProdConsByCons = (dStrgChildren != 0 ? batProdConsByCons / dStrgChildren : 0.);

                for (FluxNode c : children) {
                    c.balancingState(i,
                        fConsoFromGrid,
                        fConsoFromProd,
                        fConsoFromBat,
                        fProdConsByBat,
                        fProdConsByConsumers,
                        fProdSentToGrid,
                        fBatConsFromGrid,
                        fBatProdConsByCons
                    );
                }
            } else if ((dConsoGlobal == 0. ^ dProdGlobal == 0.) && (fConsoFromGrid != 0. ^ fProdSentToGrid != 0.)) {

                consoFromGrid = (dConsoGlobal * fConsoFromGrid) + stateFluxNodeDTO.getpConsoFromGrid().get(i).getMeasure();
                consoFromBat = (dConsoGlobal * fConsoFromBat) + stateFluxNodeDTO.getpConsoFromBat().get(i).getMeasure();
                consoFromProd = (dConsoGlobal * fConsoFromProd) + stateFluxNodeDTO.getpConsoFromProd().get(i).getMeasure();
                prodConsByBat = (dProdGlobal * fProdConsByBat) + stateFluxNodeDTO.getpProdConsByBat().get(i).getMeasure();
                prodConsByConsumers = (dProdGlobal * fProdConsByConsumers) + stateFluxNodeDTO.getpProdConsByConsumers().get(i).getMeasure();
                prodSentToGrid = (dProdGlobal * fProdSentToGrid) + stateFluxNodeDTO.getpProdSentToGrid().get(i).getMeasure();

                dProdGlobal = prodGlobal - (prodConsByConsumers + prodConsByBat + prodSentToGrid);
                dConsoGlobal = consoGlobal - (consoFromBat + consoFromGrid + consoFromProd);

                stateFluxNodeDTO.getdCons().get(i).setMeasure(dConsoGlobal);
                stateFluxNodeDTO.getdProd().get(i).setMeasure(dProdGlobal);
                stateFluxNodeDTO.getpConsoFromGrid().get(i).setMeasure(consoFromGrid);
                stateFluxNodeDTO.getpConsoFromProd().get(i).setMeasure(consoFromProd);
                stateFluxNodeDTO.getpConsoFromBat().get(i).setMeasure(consoFromBat);
                stateFluxNodeDTO.getpProdSentToGrid().get(i).setMeasure(prodSentToGrid);
                stateFluxNodeDTO.getpProdConsByConsumers().get(i).setMeasure(prodConsByConsumers);
                stateFluxNodeDTO.getpProdConsByBat().get(i).setMeasure(prodConsByBat);

                for (FluxNode c : children) {
                    c.balancingState(i,
                        fConsoFromGrid,
                        fConsoFromProd,
                        fConsoFromBat,
                        fProdConsByBat,
                        fProdConsByConsumers,
                        fProdSentToGrid,
                        fBatConsFromGrid,
                        fBatProdConsByCons
                    );
                }
            }//if (dp = 0 or dc=0) then not balancing
        } else {//it's a border node
            diff = dProdGlobal - dConsoGlobal;

            if (diff < 0) {
                consoFromGrid = -diff;
                prodSentToGrid = 0.;

                consoFromProd = dProdGlobal; //ne considere pas la production de la battery
                consoFromBat = (dStrgGlobal > 0 ? 0. : Math.abs(dStrgGlobal)); //>0 batt consume power ; <0 batt produit power
                prodConsByConsumers = dProdGlobal; //consFromProd
                prodConsByBat = (dStrgGlobal <= 0 ? 0. : dConsoGlobal >= dStrgGlobal ? 0. : (dStrgGlobal - dConsoGlobal));
                batConsFromGrid = (dStrgGlobal <= 0 ? 0. : dStrgGlobal - dProdGlobal);
                batProdConsByCons = consoFromBat;

            } else if (diff > 0) {
                consoFromGrid = 0.;
                prodSentToGrid = diff;

                consoFromProd = dConsoGlobal + (dStrgGlobal > 0 ? dStrgGlobal : 0.); //en considerant battery
                consoFromBat = (dStrgGlobal > 0 ? 0. : dProdGlobal > dConsoGlobal ? 0. : (dConsoGlobal - dProdGlobal)); //>0 batt consume power ; <0 batt produit power
                prodConsByConsumers = dConsoGlobal;
                prodConsByBat = (dStrgGlobal < 0 ? 0. : dStrgGlobal);
                batConsFromGrid = 0.;
                batProdConsByCons = consoFromBat;
            } else {
                consoFromGrid = 0.;
                consoFromProd = dConsoGlobal + (dStrgGlobal > 0 ? dStrgGlobal : 0.); //en considerant la production de la battery aussi
                consoFromBat = (dStrgGlobal > 0 ? 0. : dStrgGlobal); //>0 batt consume power ; <0 batt produit power
                prodConsByConsumers = dProdGlobal;
                prodConsByBat = (dStrgGlobal < 0 ? 0. : dStrgGlobal);
                prodSentToGrid = 0.;
                batConsFromGrid = 0.;
                batProdConsByCons = consoFromBat;
            }

            dProdGlobal = prodGlobal - (
                (prodConsByConsumers + stateFluxNodeDTO.getpProdConsByConsumers().get(i).getMeasure()) +
                    (prodConsByBat + stateFluxNodeDTO.getpProdConsByBat().get(i).getMeasure()) +
                    (prodSentToGrid + stateFluxNodeDTO.getpProdSentToGrid().get(i).getMeasure()));
            dConsoGlobal = consoGlobal - (
                (consoFromBat + stateFluxNodeDTO.getpConsoFromBat().get(i).getMeasure()) +
                    (consoFromGrid + stateFluxNodeDTO.getpConsoFromGrid().get(i).getMeasure()) +
                    (consoFromProd + stateFluxNodeDTO.getpConsoFromProd().get(i).getMeasure()));
            dStrgGlobal = 0.;

            //add facteurs de correction de consommation et production
            stateFluxNodeDTO.getdCons().get(i).setMeasure(dConsoGlobal);
            stateFluxNodeDTO.getdProd().get(i).setMeasure(dProdGlobal);
            stateFluxNodeDTO.getpConsoFromGrid().get(i).setMeasure(consoFromGrid + stateFluxNodeDTO.getpConsoFromGrid().get(i).getMeasure());
            stateFluxNodeDTO.getpConsoFromProd().get(i).setMeasure(consoFromProd + stateFluxNodeDTO.getpConsoFromProd().get(i).getMeasure());
            stateFluxNodeDTO.getpConsoFromBat().get(i).setMeasure(consoFromBat + stateFluxNodeDTO.getpConsoFromBat().get(i).getMeasure());
            stateFluxNodeDTO.getpProdSentToGrid().get(i).setMeasure(prodSentToGrid + stateFluxNodeDTO.getpProdSentToGrid().get(i).getMeasure());
            stateFluxNodeDTO.getpProdConsByConsumers().get(i).setMeasure(prodConsByConsumers + stateFluxNodeDTO.getpProdConsByConsumers().get(i).getMeasure());
            stateFluxNodeDTO.getpProdConsByBat().get(i).setMeasure(prodConsByBat + stateFluxNodeDTO.getpProdConsByBat().get(i).getMeasure());

            //balancing children
            fConsoFromGrid = (dConsChildren != 0 ? consoFromGrid / dConsChildren : 0.);
            fConsoFromBat = (dConsChildren != 0 ? consoFromBat / dConsChildren : 0.);
            fConsoFromProd = (dConsChildren != 0 ? consoFromProd / dConsChildren : 0.);
            fProdConsByBat = (dProdChildren != 0 ? prodConsByBat / dProdChildren : 0.);
            fProdConsByConsumers = (dProdChildren != 0 ? prodConsByConsumers / dProdChildren : 0.);
            fProdSentToGrid = (dProdChildren != 0 ? prodSentToGrid / dProdChildren : 0.);
            fBatConsFromGrid = (dStrgChildren != 0 ? batConsFromGrid / dStrgChildren : 0.);
            fBatProdConsByCons = (dStrgChildren != 0 ? batProdConsByCons / dStrgChildren : 0.);

            for (FluxNode c : children) {
                c.balancingState(i,
                    fConsoFromGrid,
                    fConsoFromProd,
                    fConsoFromBat,
                    fProdConsByBat,
                    fProdConsByConsumers,
                    fProdSentToGrid,
                    fBatConsFromGrid,
                    fBatProdConsByCons
                );
            }
        }
    }

    private List<MeasuredData> filterByType(List<StateFluxNodeDTO> node, DataType type) {
        List<MeasuredData> filteredList;
        switch (type) {
            case D_CONS:
                filteredList = node.stream()
                    .map(s -> s.getdCons())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList());
                break;
            case D_PROD:
                filteredList = node.stream()
                    .map(s -> s.getdProd())
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList());
                break;
            case D_STRG:
                filteredList = node.stream()
                    .map(s -> {
                        if (s.getdStrg().size() == s.getpBatGlobal().size()) {
                            return s.getdStrg();
                        } else {
                            return s.getpBatGlobal();
                        }
                    })
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                        MeasuredData::getInstant,
                        LinkedHashMap::new,
                        Collectors.summingDouble(MeasuredData::getMeasure)))
                    .entrySet().stream()
                    .map(p -> new MeasuredData(p.getKey(), p.getValue(), true, DataType.POWER))
                    .collect(Collectors.toList());
                break;
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


}
