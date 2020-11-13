"""
Final modification: 04/03/2019

@author: zhuj
"""

import sys
import json
import time as tm

from.optimize import Problem, Guess, Condition, Dynamics
from.ProblemOPEX import Params, interp_data, BillOptimProblem
from.DataPrepare import PrepareData


def main():

    inputFilename = sys.argv[1]+"/"+sys.argv[2]+"battery_management_input.json"
    outputFilename = sys.argv[1]+"/"+sys.argv[2]+"battery_management_output.json"
    with open(inputFilename) as infile:
        jsonInput = json.load(infile)

    # ========================
    ## user defined optimisation settings
    # TODO add to optimization settings
    # => Insert to properties file and set as optional param to pass in the WS request withJson to overwrite default
    control_reg_flag = False  # if true, add control regularisation
    Etf_constraint = True  # if false, constraint E(T)=Ef will be ignored
    n_collocpoints = 144  # choose the number of discretisation points


    # ========================
    ## Prepare data for the optimisation problem
    Em, Ep, E_0, E_tf, Pcp, Pdp, vec_E_Plim, T, rho_c, rho_d, alpha, Conso, \
    PPV, prix_achat, prix_vente, Epmin, Epmax, Ps, alpha_peak, Date, DT = \
        PrepareData(json.loads(jsonInput))

    if Etf_constraint == False:
        E_tf = None


    # ========================
    #  optimisation settings
    obj_param = Params(Em, Ep, E_0, E_tf, Pcp, Pdp, vec_E_Plim, T, rho_c, rho_d, alpha, Conso, PPV,
                       prix_achat, prix_vente, Epmin, Epmax, Ps, alpha_peak, Date, DT,
                       control_reg_flag)  # create parameter object
    obj_problem = BillOptimProblem(obj_param)  # create problem object

    time_init = [0.0, obj_param.tf]  # optim time iterval (initial)
    n = [n_collocpoints]  # number of nodes
    num_states = [1]  # number of state
    num_controls = [4]  # number of controls
    method = 'LGL'  # collocation method: LGL
    maxIter = 10
    maxIter_slsqp = 10  # maximum iteration = maxIter*maxIter_slsqp
    cost_tol = 1e-4  # convergence tolerance:   |cost_{k+1} - cost_{k} | <= cost_tol

    prob = Problem(time_init, n, num_states, num_controls, maxIter, method)
    obj_problem.initialisation(prob, obj_param)
    prob.dynamics = [obj_problem.dynamics]
    prob.knot_states_smooth = [True]
    prob.cost = obj_problem.cost
    prob.running_cost = obj_problem.running_cost
    prob.equality = obj_problem.equality
    prob.inequality = obj_problem.inequality

    time_new = prob.time_update()
    delta_Conso_, delta_PV_, prix_achat_, prix_vente_ = interp_data(time_new, obj_param)
    obj_param.update_data(delta_Conso_, delta_PV_, prix_achat_, prix_vente_)


    # ========================
    # Main Process: assign problem to SQP solver
    time_start = tm.time()

    opt=prob.solve(obj_param, obj_problem.display_func, ftol=cost_tol, maxiter=maxIter_slsqp)

    time_end = tm.time()


    # ========================
    # Post Process
    # ------------------------
    # Create JSON response with optimisation results : states & controls
    jsonOutput = obj_problem.write_json_output(opt, obj_param, prob, time_start, time_end)

    # Send response to JAVA
    # Printing json response to file
    with open(outputFilename, 'w+') as outfile:
        json.dump(jsonOutput, outfile)

    return



if __name__ == "__main__":

    main()
