"""
Final modification: 04/03/2019

@author: zhuj
"""

from __future__ import print_function
import autograd.numpy as np
from scipy import interpolate as interp
import matplotlib.pyplot as plt
from.optimize import Problem, Guess, Condition, Dynamics


# ========================   
## some useful sub-routines
def return_positive_part(x):
    x[x<0.]=0.
    return x

def return_negative_part(x):
    x[x>=0.]=0.
    return x

def lagrange (x ,i , xm ):
    # Evaluates the i-th Lagrange polynomial at x based on grid data xm
    n=len( xm )-1
    y=1.
    for j in range ( n+1 ):
        if i!=j:
            y *= (x-xm[j])/(xm[i]-xm[j])
    return y

def interpolation (x , xm , ym ):
    # Lagrange interpolate polynomial evaluated at x
    n=len( xm )-1
    lagrpoly = np.array ([ lagrange (x ,i , xm ) for i in range ( n+1 )])
    y = np.dot( ym , lagrpoly )
    return y
        


# ========================   
#  problem parameters
class Params:
    def __init__(self,Em,Ep,E0,Ef,Pcp,Pdp,vec_E_Plim,T,rho_c,rho_d,alpha,Conso,PPV,\
                      prix_achat,prix_vente,Epmin,Epmax,Ps,alpha_peak,Date,DT,
                      control_regularisation_flag=False):
        self.Emin = Em              # minimum E 
        self.Emax = Ep              # maximum E
        self.Ecapa_min = Epmin      # minimum E capacity (not used so far)
        self.Ecapa_max = Epmax      # maximum E capacity (not used so far)
        self.Pcmin = 0.             #
        self.Pdmin = 0.        
        self.Pcmax = np.reshape(Pcp,len(Pcp)) 
        self.Pdmax = np.reshape(Pdp,len(Pdp)) 
        self.vec_E_Plim=np.reshape(vec_E_Plim,len(vec_E_Plim)) 
        self.lambdaConso_min=0.
        self.lambdaPV_min=0.        
        self.lambdaConso_max=1.
        self.lambdaPV_max=1.
        
        self.time_ori = np.arange(0,T,T/(np.size(Conso)))
        self.tf = self.time_ori[-1]
        self.alpha = alpha
        self.E_0 = E0
        self.E_tf= Ef
        self.Conso=np.reshape(Conso,len(Conso))
        self.PV=np.reshape(PPV,len(PPV))
        self.prix_achat=np.reshape(prix_achat,len(prix_achat))
        self.prix_vente=np.reshape(prix_vente,len(prix_vente))
        self.pb_size=np.size(Conso)
        self.rho_c=rho_c
        self.rho_d=rho_d
        self.prix_achat_=self.prix_achat
        self.prix_vente_=self.prix_vente
        self.PS=Ps
        self.alpha_peak=alpha_peak
        
        self.Date=Date
        self.DT=DT
        
        self.tol_chg_dchg=1e-6       # P_chg*P_dchg <= tol_chg_dchg   

        self.interp_pcp = interp.interp1d(self.vec_E_Plim, self.Pcmax, kind='linear',fill_value="extrapolate")
        self.interp_pdp = interp.interp1d(self.vec_E_Plim, self.Pdmax, kind='linear',fill_value="extrapolate")
        
        if control_regularisation_flag:
            self.reg_coefs=np.array([1e-2,1e-2,1e-1,1e-1])    
        else:
            self.reg_coefs=np.array([0.,0.,0.,0.])  
        
        delta_Conso=self.Conso-self.PV
        delta_Conso[delta_Conso<=0.0]=0.0
        delta_Conso=np.reshape(delta_Conso,len(delta_Conso))   
        self.delta_Conso_=delta_Conso

        delta_PV=self.PV-self.Conso
        delta_PV[delta_PV<=0.0]=0.0
        delta_PV=np.reshape(delta_PV,len(delta_PV)) 
        self.delta_PV_=delta_PV         


    def update_data(self,delta_Conso,delta_PV,prix_achat,prix_vente):
        self.delta_Conso_ = delta_Conso
        self.delta_PV_  = delta_PV
        self.prix_achat_= prix_achat
        self.prix_vente_= prix_vente
        
    def get_update_data(self):
        return self.delta_Conso_,self.delta_PV_,self.prix_achat_,self.prix_vente_


 
       
def interp_data(time_new,obj):
    delta     = obj.Conso - obj.PV
#    delta     = np.reshape(delta,obj.pb_size)
    prix_achat= np.reshape(obj.prix_achat,obj.pb_size)
    prix_vente= np.reshape(obj.prix_vente,obj.pb_size)   
    time_ori  = np.reshape(obj.time_ori,obj.pb_size)               
    
    f_delta = interp.interp1d(time_ori, delta, fill_value="extrapolate")
    f_prix_achat = interp.interp1d(time_ori, prix_achat, fill_value="extrapolate")
    f_prix_vente = interp.interp1d(time_ori, prix_vente, fill_value="extrapolate")
    
    delta_ = f_delta(time_new)
    prix_achat_ = f_prix_achat(time_new)
    prix_vente_ = f_prix_vente(time_new)
    
    delta_Conso_ = delta_
    delta_PV_    = -delta_
    delta_Conso_ =  return_positive_part(delta_Conso_)
    delta_PV_    =  return_positive_part(delta_PV_)
    
    obj.update_data(delta_Conso_,delta_PV_,prix_achat_,prix_vente_)  
    return delta_Conso_,delta_PV_,prix_achat_,prix_vente_



      
# ========================   
#  define optimal control problem  
class BillOptimProblem:
    def __init__(self, obj):
        self.J  = [] 
        self.J_penalty = []
        self.indice_interp=True;
        self.prix_achat_ = obj.prix_achat_ 
        self.prix_vente_ = obj.prix_vente_
        self.delta_Conso_= obj.delta_Conso_
        self.delta_PV_   = obj.delta_PV_        
        self.time = obj.time_ori
        
    #  problem dynamics
    def dynamics(self, prob, obj, section):
        E   = prob.states(0, section)            # state
        lambdaPV = prob.controls(0, section)     # control 1
        lambdaConso = prob.controls(1, section)  # control 2
        Pc = prob.controls(2, section)           # control 3
        Pd = prob.controls(3, section)           # control 4
        
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj.get_update_data()
        self.prix_achat_ = prix_achat_
        self.prix_vente_ = prix_vente_
        self.delta_Conso_= delta_Conso_
        self.delta_PV_   = delta_PV_
        
        dx = Dynamics(prob, section)    
        dx[0] = obj.rho_c*Pc - Pd \
                - lambdaConso*delta_Conso_ \
                + obj.rho_c*lambdaPV*delta_PV_ \
                - obj.alpha*E
                
        return dx()


    #  equality constraints
    def equality(self, prob, obj):
        E   = prob.states_all_section(0)           # state
        lambdaPV = prob.controls_all_section(0)    # control 1
        lambdaConso = prob.controls_all_section(1) # control 2
        Pc = prob.controls_all_section(2)          # control 3
        Pd = prob.controls_all_section(3)          # control 4
        tf  = prob.time_final(-1)
    
        result = Condition()
    
        # event condition
        result.equal(E[0], obj.E_0)    
        if obj.E_tf is not None:
            result.equal(E[-1], obj.E_tf)
        result.equal(tf, obj.tf)
        
        return result()


    #  inequality constraints
    def inequality(self, prob, obj):
        E   = prob.states_all_section(0)           # state
        lambdaPV = prob.controls_all_section(0)    # control 1
        lambdaConso = prob.controls_all_section(1) # control 2
        Pc = prob.controls_all_section(2)          # control 3
        Pd = prob.controls_all_section(3)          # control 4
        time_new = prob.time_update() 
        
        result = Condition()
        
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj.get_update_data()   
        self.prix_achat_ = prix_achat_
        self.prix_vente_ = prix_vente_
        self.delta_Conso_= delta_Conso_
        self.delta_PV_   = delta_PV_
        self.time = time_new
        
        try:
            Elist=list(E[:, ])
            Pc_max=np.zeros(len(Elist))
            Pd_max=np.zeros(len(Elist))
            for i in np.arange(len(Elist)):
                Pc_max[i]=obj.interp_pcp(Elist[i]._value)
                Pd_max[i]=obj.interp_pdp(Elist[i]._value)
        except:
            Pc_max = obj.interp_pcp(E)
            Pd_max = obj.interp_pdp(E)   
                    
        
        # lower bounds
        result.lower_bound(E, obj.Emin)
        result.lower_bound(Pc, obj.Pcmin)
        result.lower_bound(Pd, obj.Pdmin)
        result.lower_bound(lambdaPV, obj.lambdaPV_min)
        result.lower_bound(lambdaConso, obj.lambdaConso_min)
        result.lower_bound(Pd+lambdaConso*delta_Conso_, obj.Pdmin)
        result.lower_bound(Pc+lambdaPV*delta_PV_, obj.Pcmin)    
        
        # upper bounds
        result.upper_bound(E, obj.Emax)
        result.upper_bound(Pc-Pc_max,0.)
        result.upper_bound(Pd-Pd_max,0.)
        result.upper_bound(lambdaPV, obj.lambdaPV_max)
        result.upper_bound(lambdaConso, obj.lambdaConso_max)    
        result.upper_bound(Pd+lambdaConso*delta_Conso_-Pd_max,0.)
        result.upper_bound(Pc+lambdaPV*delta_PV_-Pc_max,0.)
        result.upper_bound((Pc+lambdaPV*delta_PV_)*(Pd+lambdaConso*delta_Conso_),obj.tol_chg_dchg)
        
        return result()


    #  cost functional
    def cost(self, prob, obj):
        lambdaPV = prob.controls_all_section(0)    # control 1
        lambdaConso = prob.controls_all_section(1) # control 2
        Pc = prob.controls_all_section(2)          # control 3
        Pd = prob.controls_all_section(3)          # control 4
        time = prob.time_update()
        
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj.get_update_data()

        P_compteur = delta_Conso_ - delta_PV_ + Pc- obj.rho_d*Pd
        P_depasse  = P_compteur - obj.PS
        
#        k_depasse=1e2
        k_depasse=700/max(P_compteur)  # exp(710)=inf
        P_penalty = P_depasse
        for i in np.arange(len(P_depasse)):
            P_penalty[i] = np.log(1 + np.exp(k_depasse*P_depasse[i]))/k_depasse
        nodes, weight, D  = prob.method_LGL(len(time))
        integrand = P_penalty*P_penalty 
        J_penalty = obj.alpha_peak*pow((time[-1]-time[0])/2.0*sum(integrand * weight),0.5)   
        self.J_penalty += [J_penalty]               
        
        return J_penalty
    
        
    #  running cost
    def running_cost(self, prob, obj):
        lambdaPV = prob.controls_all_section(0)    # control 1
        lambdaConso = prob.controls_all_section(1) # control 2
        Pc = prob.controls_all_section(2)          # control 3
        Pd = prob.controls_all_section(3)          # control 4
        time = prob.time_update()
        
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj.get_update_data()
        
        self.prix_achat_ = prix_achat_
        self.prix_vente_ = prix_vente_
        self.delta_Conso_= delta_Conso_
        self.delta_PV_   = delta_PV_
        
        J_array = prix_achat_*(delta_Conso_*(1-obj.rho_d*lambdaConso)+Pc) \
                - prix_vente_*(delta_PV_*(1-lambdaPV)+obj.rho_d*Pd) 
                
        J=np.trapz(J_array,time)         
        self.J += [J]    
        
        reg_controls = obj.reg_coefs[0]*lambdaPV*lambdaPV \
                      +obj.reg_coefs[1]*lambdaConso*lambdaConso \
                      +obj.reg_coefs[2]*Pc*Pc \
                      +obj.reg_coefs[3]*Pd*Pd
        return J_array+reg_controls
    

    #  display function
    def display_func(self):
        print('cost =',self.J[-1], '+', self.J_penalty[-1])

    
    # initialization
    def initialisation(self, prob, obj):
        # Initial parameter guess
        E_init = Guess.constant(prob.time_all_section, obj.E_0)
        lambdaConso_init = Guess.constant(prob.time_all_section, 0*obj.lambdaConso_max/2)
        lambdaPV_init = Guess.constant(prob.time_all_section, 0*obj.lambdaPV_max/2)
#        Pc_init = Guess.linear(prob.time_all_section, obj_param.Pcmin, obj_param.Pcmax)
#        Pd_init = Guess.linear(prob.time_all_section, obj_param.Pdmin, obj_param.Pdmax)
        Pc_init = Guess.constant(prob.time_all_section, 0*obj.Pcmin)
        Pd_init = Guess.constant(prob.time_all_section, 0*obj.Pdmin)
        
        prob.set_states_all_section(0, E_init)        
        prob.set_controls_all_section(0, lambdaPV_init)
        prob.set_controls_all_section(1, lambdaConso_init)        
        prob.set_controls_all_section(2, Pc_init)
        prob.set_controls_all_section(3, Pd_init)
        
        return prob


    # reinitialization
    def reinitialisation(self, prob, prob_new):
        # Initial parameter guess
        time=prob.time_all_section
        E   = prob.states_all_section(0)           # state
        lambdaPV = prob.controls_all_section(0)    # control 1
        lambdaConso = prob.controls_all_section(1) # control 2
        Pc = prob.controls_all_section(2)          # control 3
        Pd = prob.controls_all_section(3)          # control 4
        
        time_new=prob_new.time_all_section
        
        E_init = Guess.interpolation(time,E,time_new)
        lambdaConso_init = Guess.interpolation(time,lambdaConso,time_new)
        lambdaPV_init = Guess.interpolation(time,lambdaPV,time_new)
        Pc_init = Guess.interpolation(time,Pc,time_new)
        Pd_init = Guess.interpolation(time,Pd,time_new)

#        time_interp=time_new
#        E_init = interpolation(time_interp,time,E )
#        lambdaConso_init = interpolation(time_interp,time,lambdaConso)
#        lambdaPV_init = interpolation(time_interp,time,lambdaPV)
#        Pc_init = interpolation(time_interp,time,Pc)
#        Pd_init = interpolation(time_interp,time,Pd)
        
        prob_new.set_states_all_section(0, E_init)
        prob_new.set_controls_all_section(0, lambdaPV_init)        
        prob_new.set_controls_all_section(1, lambdaConso_init)
        prob_new.set_controls_all_section(2, Pc_init)
        prob_new.set_controls_all_section(3, Pd_init)
       
        return prob_new

    
    ## --- methods ---    
    # return cost function int_0^t f(x) dx
    def bill_running_part_integral(self,prob,obj,lambdaPV,lambdaConso,Pc,Pd):    
        time_new = prob.time_update()         
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj.get_update_data() 
        
        J_time=[]
        for i in np.arange(0,len(time_new)):
            J_running = prix_achat_[i]*(delta_Conso_[i]*(1-obj.rho_d*lambdaConso[i])+Pc[i]) \
                      - prix_vente_[i]*(delta_PV_[i]*(1-lambdaPV[i])+obj.rho_d*Pd[i])
            if i>0:     
                J_time += [ J_time[-1] + J_running*(time_new[i]-time_new[i-1]) ] 
            else:
                J_time += [ J_running*time_new[i] ]                 
                
        return J_time
    
    # get interpolated data
    def interped_data(self):
        return self.delta_Conso_,self.delta_PV_,self.prix_achat_,self.prix_vente_

    # get running cost f^0(t)
    def bill_running_part(self,obj,lambdaPV,lambdaConso,Pc,Pd):        
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj.get_update_data()  
        
        bill_running = prix_achat_*(delta_Conso_*(1-obj.rho_d*lambdaConso)+Pc) \
                      - prix_vente_*(delta_PV_*(1-lambdaPV)+obj.rho_d*Pd) 
        return bill_running

    # get cost (penalty facture part)
    def bill_penalty_part(self,obj,time,lambdaPV,lambdaConso,Pc,Pd): 
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj.get_update_data()
        P_penalty = delta_Conso_ - delta_PV_ + Pc- obj.rho_d*Pd - obj.PS  
        P_penalty = return_positive_part(P_penalty)
        
        bill_penalty = obj.alpha_peak * pow(np.trapz(P_penalty*P_penalty,time),0.5) 
           
        return bill_penalty
        
    
    # write optimisation report to file 
    def write_opt_report(self,opt,obj_param,prob,savefig_dir,time_start,time_end): 
        time         = prob.time_update()            # time 
        E   = prob.states_all_section(0)           # state
        lambdaPV = prob.controls_all_section(0)    # control 1
        lambdaConso = prob.controls_all_section(1) # control 2
        Pc = prob.controls_all_section(2)          # control 3
        Pd = prob.controls_all_section(3)          # control 4
           
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj_param.get_update_data()        
        
        integrand         = self.bill_running_part(obj_param,lambdaPV,lambdaConso,Pc,Pd)
        integrand_sansbat = self.bill_running_part(obj_param,lambdaPV*0,lambdaConso*0,Pc*0,Pd*0) 
        nodes, weight, D  = prob.method_LGL(len(time))
        cost_nomral_part         = (time[-1] - time[0])/2.0*sum(integrand * weight) 
        cost_normal_part_sansbat = (time[-1] - time[0])/2.0*sum(integrand_sansbat * weight) 
        cost_penalty_part = self.bill_penalty_part(obj_param,time,lambdaPV,lambdaConso,Pc,Pd) 
        cost_penalty_part_sansbat = self.bill_penalty_part(obj_param,time,lambdaPV*0,lambdaConso*0,Pc*0,Pd*0)
        
        f = open(savefig_dir, "w")
        f.write('Execution time='+str(time_end - time_start)+'s')
        f.write('\n---------------------')
        f.write('\n Facture fin journée sans batterie='
                +str(cost_normal_part_sansbat+cost_penalty_part_sansbat)+' euros')
        f.write('\n partie pénalité en cas de dépassement ='
                +str(cost_penalty_part_sansbat)+' euros')
        f.write('\n partie coût électricité ='
                +str(cost_normal_part_sansbat)+' euros') 
        
        f.write('\n\n Facture fin journée avec batterie='
                +str(cost_nomral_part+cost_penalty_part)+' euros')
        f.write('\n partie pénalité en cas de dépassement ='
                +str(cost_penalty_part)+' euros')
        f.write('\n partie coût électricité ='
                +str(cost_nomral_part)+' euros')          
        f.close()



    # write optimisation report to file
    def write_json_output(self,opt,obj_param,prob,time_start,time_end):
        time         = prob.time_update()            # time
        E   = prob.states_all_section(0)           # state
        lambdaPV = prob.controls_all_section(0)    # control 1
        lambdaConso = prob.controls_all_section(1) # control 2
        Pc = prob.controls_all_section(2)          # control 3
        Pd = prob.controls_all_section(3)          # control 4
        delta_Conso_,delta_PV_,prix_achat_,prix_vente_ = obj_param.get_update_data()

        jsonOutput = {}
        jsonOutput["start"] = time_start
        jsonOutput["end"] = time_end
        jsonOutput["result"] = []
        for i in range(0, len(time)):
            jsonOutput["result"].append({})
            jsonOutput["result"][i]["time"] = time[i]
            jsonOutput["result"][i]["PPV"] = delta_PV_[i]
            jsonOutput["result"][i]["Conso"] = delta_Conso_[i]
            jsonOutput["result"][i]["pBat"] = delta_PV_[i]  # FIXME set to pBat
            jsonOutput["result"][i]["soc"] = delta_PV_[i]   # FIXME set to SoC

        return jsonOutput


    
    ## ------------------------
    # Visualizetion
    def plot_opt_results(self,flag_savefig,fig_resolution,savefig_dir,
                         prob,obj_param,obj_problem,n,addiname):
        # Convert parameter vector to variable
        E            = prob.states_all_section(0)    # state
        lambdaPV     = prob.controls_all_section(0)  # control 1
        lambdaConso  = prob.controls_all_section(1)  # control 2
        Pc           = prob.controls_all_section(2)  # control 3
        Pd           = prob.controls_all_section(3)  # control 4
        time         = prob.time_update()            # time
        Ecapa = obj_param.Emax*np.ones([len(E),1])
        
        Cost_Time    = obj_problem.bill_running_part_integral\
                        (prob,obj_param,lambdaPV,lambdaConso,Pc,Pd)
        Cost_Time_sansbat = obj_problem.bill_running_part_integral\
                            (prob,obj_param,lambdaPV*0,lambdaConso*0,Pc*0,Pd*0)
                            
        deltaConso,deltaPV,prixachat,prixvente = obj_problem.interped_data()
        charge_batterie = Pc + lambdaPV*deltaPV          # puissance => charge de la batterie
        decharge_batterie = Pd + lambdaConso*deltaConso  # puissance => decharge de la batterie
        P_bat = obj_param.rho_c*charge_batterie - decharge_batterie
        facture_vente=obj_param.prix_vente_*(deltaPV-deltaPV*lambdaPV+obj_param.rho_c*Pd)
        facture_achat=obj_param.prix_achat_*(deltaConso-obj_param.rho_d*deltaConso*lambdaConso+Pd)
        Pc_max = obj_param.interp_pcp(E)
        Pd_max = obj_param.interp_pdp(E) 

        
        cost_penalty_part = self.bill_penalty_part(obj_param,time,lambdaPV,lambdaConso,Pc,Pd) 
        cost_penalty_part_sansbat = self.bill_penalty_part(obj_param,time,lambdaPV*0,lambdaConso*0,Pc*0,Pd*0)
        
        
## ----------  for test -----------------
#        def lagrange (x ,i , xm ):
#            """
#            Evaluates the i-th Lagrange polynomial at x based on grid data xm
#            """
#            n=len( xm )-1
#            y=1.
#            for j in range ( n+1 ):
#                if i!=j:
#                    y *= (x-xm[j])/(xm[i]-xm[j])
#            return y
#
#        def interpolation (x , xm , ym ):
#            n=len( xm )-1
#            lagrpoly = np.array ([ lagrange (x ,i , xm ) for i in range ( n+1 )])
#            y = np.dot( ym , lagrpoly )
#            return y
#        
#        time_interp=np.arange(0,24,24/400)
#        E_interp = interpolation ( time_interp , time , E )
#        dE_interp = (E_interp[1:len(E_interp)]-E_interp[0:len(E_interp)-1])/ \
#                    (time_interp[1:len(E_interp)]-time_interp[0:len(E_interp)-1])
#        P_bat_interp = interpolation ( time_interp , time , P_bat )
#        plt.figure()
#        plt.plot(time_interp[1:len(E_interp)],dE_interp,'r')
#        plt.plot(time_interp,P_bat_interp)
#
#        dE = (E[1:len(E)]-E[0:len(E)-1])/ \
#             (time[1:len(E)]-time[0:len(E)-1])
#        plt.figure()
#        plt.plot(time[1:len(E)],dE,'r')
#        plt.plot(time,P_bat)        
            
        # state E(t)
        plt.figure(dpi=fig_resolution)
        plt.subplot(211)
        plt.plot(time, E,"r+-", label="E(t)", LineWidth=2)
        #plt.plot(time_interp,E_interp,'ro-',label="E(t) interp")
        plt.plot(time, Ecapa, 'k-.', label='E capacité')
        plt.plot(time, charge_batterie, "b+-", 
                 label="$P_{charge} (t))$")
        plt.plot(time, decharge_batterie, "g+-", 
                 label="$P_{decharge} (t)$")
        plt.plot(time,P_bat,'c+-',
                 label="$P_{bat} (t)$")
        plt.plot(time,Pc_max,'-.',label='$P_{charge}^{max} (E(t))$')
        plt.plot(time,Pd_max,'-.',label='$P_{d\'echarge}^{max} (E(t))$')
        #plt.plot(time, P_bat, marker="o", label="$\dot{E} (t)$")
        #for line in prob.time_knots():
        #    plt.axvline(line, color="k", alpha=0.5)
        plt.grid()
        plt.xlabel("time [h]")
        plt.ylabel("E [kWh] et Puissance [kW]")
        plt.legend(loc='upper left', bbox_to_anchor=(1, 1))
        plt.subplot(212)
        plt.plot(time, Cost_Time, marker="+", label="facture cumulee")
        plt.plot(time, Cost_Time_sansbat,marker="+", label='facture cumulee sans bat')
        plt.plot(time, cost_penalty_part*np.ones(len(time)), 
                 '-.', label='pénalité dépassement')
        plt.plot(time, cost_penalty_part_sansbat*np.ones(len(time)), 
                 '-.', label='pénalité dépassement sans bat')        
        plt.grid()
        plt.xlabel("time [h]")
        plt.ylabel("Cumulated electricity bill")        
        plt.legend(loc='upper left', bbox_to_anchor=(1, 0.8))
#        plt.subplot(313)
#        plt.plot(time, facture_vente, marker="+", label='facture vente (t)')
#        plt.plot(time, facture_achat, marker="+", label='facture achat (t)')
#        plt.plot(time, facture_achat-facture_vente, "o", label='facture totale (t)')
#        plt.grid()
#        plt.xlabel("time [h]")
#        plt.ylabel("Instant electricity bill") 
#        plt.legend(loc='upper left', bbox_to_anchor=(1, 0.8))      
        if(flag_savefig): 
            plt.savefig(savefig_dir + "State_n=" + str(n) + addiname
                         + ".png",bbox_inches="tight")
        plt.clf()
    
    
        # controls
        plt.figure(dpi=fig_resolution)
        plt.subplot(311)
        plt.plot(time, deltaConso, marker='+', 
                 label='$\Delta$ Conso')
        plt.plot(time, lambdaConso*deltaConso, marker="+", 
                 label="$\lambda_{conso} \Delta$ Conso")
        plt.legend(loc='upper left', bbox_to_anchor=(1, 0.6))
        plt.grid()
        plt.xlabel("time [h]")
        plt.ylabel("Puissance [kW]")
        plt.subplot(312)
        plt.plot(time, deltaPV, marker='+', 
                 label='$\Delta$ PV')
        plt.plot(time, lambdaPV*deltaPV, marker="+", 
                 label="$\lambda_{PV} \Delta$ PV")
        plt.grid()
        plt.xlabel("time [h]")
        plt.ylabel("Puissance [kW]")
        plt.legend(loc='upper left', bbox_to_anchor=(1, 0.6))
        plt.subplot(313)
        plt.plot(time, Pc, marker="+", label="$P_c$")
        plt.plot(time, Pd, "-.",  label="$P_d$")
        plt.plot(time, prixachat*10, 'r', label='(Prix achat) * 10')
        plt.grid()
        plt.xlabel("time [h]")
        plt.ylabel("Puissance [kW]")
        plt.legend(loc='upper left', bbox_to_anchor=(1, 0.8))   
        if(flag_savefig): 
            plt.savefig(savefig_dir + "Control_1_n=" + str(n) + addiname 
                        + ".png",bbox_inches="tight")
        plt.clf()
    
    
        plt.figure(dpi=fig_resolution)
        plt.subplot(211)
        plt.plot(time,charge_batterie*decharge_batterie, marker="+", label="$P_{chg} P_{dchg}$")
        plt.grid()
        plt.xlabel("time [h]")
        plt.legend(loc="best")
        plt.subplot(212)
        plt.plot(time, Pc*Pd, marker="+", label="$P_c P_d$")
        plt.grid()
        plt.xlabel("time [h]")
        plt.legend(loc="best")
        if(flag_savefig): 
            plt.savefig(savefig_dir + "Control_2_n=" + str(n) + addiname 
                        + ".png",bbox_inches="tight")
        plt.clf()   
