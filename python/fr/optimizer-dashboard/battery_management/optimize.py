"""
Optimal Solution module with PseudoSpectral Method
"""

# =============================================================================
#
# Based on OpenGoddard package
#       Copyright (c) 2017 Interstellar Technologies Inc. All Rights Reserved.
#       Authors : Takahiro Inagawa, Kazuki Sakaki
#
#       Lisence : MIT Lisence
#
# Last modification: 04/03/2019 by J. ZHU (IPFEN)
# New features: 
#         Jacobian is provided by automatic differentiation package autograd
#         Callback function provided for optimize.minimize(*args)
#         Cost history can be retrieved
#
# =============================================================================


import numpy as np
from scipy import special
from scipy import interpolate
from scipy import optimize
import matplotlib.pyplot as plt
from autograd import grad,jacobian
from scipy import interpolate as interp
from scipy.special import legendre  


## Optimisation problem class
class Problem:
    """ Problem class.

    Args:
        time_init (list of float) : [time_start, time_section0, time_section0, , , time_final]
        nodes (int) : number of nodes
        number_of_states (list) : number of states
        number_of_controls (list) : number of controls
        maxIterator (int) : iteration max

    Attributes:
        nodes (int) : time nodes.
        number_of_states (int) : number of states.
        number_of_controls (int) : number of controls
        number_of_section (int) : number of section
        number_of_param (int) : number of inner variables
        div (list) : division point of inner variables
        tau : Gauss nodes
        w : weights of Gaussian quadrature
        D :  differentiation matrix of Gaussian quadrature
        time : time
        maxIterator (int) : max iterator
        time_all_section : all section time
        unit_states (list of float) : canonical unit of states
        unit_controls (list of float) : canonical unit of controls
        unit_time (float) : canonical unit of time
        p ((N,) ndarray) : inner variables for optimization
        dynamics (function) : function list, list of function of dynamics
        knot_states_smooth (list of True/False): list of states are smooth on phase knots
        cost (function) : cost function
        running_cost (function, optional) : (default = None)
        cost_derivative (function, optional) : (default = None)
        equality (function) : (default = None)
        inequality (function) : (default = None)

    """
    
    def _LegendreFunction(self, x, n):
        Legendre, Derivative = special.lpn(n, x)
        return Legendre[-1]


    ## Legendre-Gauss-Lobatto Pseudospectral method ##
    def _nodes_LGL(self, n):
        """ 
           Legendre-Gauss-Lobatto(LGL) points
        """
        roots, weight = special.j_roots(n-2, 1, 1)
        nodes = np.hstack((-1, roots, 1))
        return nodes

    def _weight_LGL(self, n):
        """ 
           Legendre-Gauss-Lobatto(LGL) weights.
        """
        nodes = self._nodes_LGL(n)
        w = np.zeros(0)
        for i in range(n):
            w = np.append(w, 2/(n*(n-1)*self._LegendreFunction(nodes[i], n-1)**2))
        return w

    def _differentiation_matrix_LGL(self, n):
        """ 
           Legendre-Gauss-Lobatto(LGL) differentiation matrix.
        """
        tau = self._nodes_LGL(n)   # LGL points           
        P_legendre=legendre(n-1)   # Legendre polynomial          
        D = np.zeros((n, n))             
        for i in range(n):
            for j in range(n):
                if i != j:
                    D[i, j] = P_legendre(tau[i])/P_legendre(tau[j]) \
                              /(tau[i] - tau[j])
                elif i == j and i == 0:
                    D[i, j] = -n*(n-1)/4.0
                elif i == j and i == n-1:
                    D[i, j] = n*(n-1)/4.0                   
        return D



    def _make_param_division(self, nodes, number_of_states, number_of_controls):
        prev = 0
        div = []
        for index, node in enumerate(nodes):
            num_param = number_of_states[index] + number_of_controls[index]
            temp = [i*(node) + prev for i in range(1, num_param + 1)]
            prev = temp[-1]
            div.append(temp)
        return div

    def _division_states(self, state, section):
        assert section < len(self.nodes), \
            "section argument out of own section range"
        assert state < self.number_of_states[section], \
            "states argument out of own states range"
        if (state == 0):
            if (section == 0):
                div_front = 0
            else:
                div_front = self.div[section-1][-1]
        else:
            div_front = self.div[section][state-1]
        div_back = self.div[section][state]
        return div_back, div_front

    def _division_controls(self, control, section):
        assert section < len(self.nodes), \
            "section argument out of own section range"
        assert control < self.number_of_controls[section], \
            "controls argument out of own controls range"
        div_front = self.div[section][self.number_of_states[section] + control - 1]
        div_back = self.div[section][self.number_of_states[section] + control]
        return div_back, div_front

    def states(self, state, section):
        """getter specify section states array

        Args:
            state (int) : state number
            section (int) : section number

        Returns:
            states ((N,) ndarray) :
                1-D array of state

        """
        div_back, div_front = self._division_states(state, section)
        return self.p[div_front:div_back] * self.unit_states[section][state]

    def states_all_section(self, state):
        """get states array

        Args:
            state (int) : state number

        Returns:
            states_all_section ((N,) ndarray) :
                1-D array of all section state

        """
        temp = np.zeros(0)
        for i in range(self.number_of_section):
            temp = np.concatenate([temp, self.states(state, i)])
        return temp

    def controls(self, control, section):
        """getter specify section controls array

        Args:
            control (int) : control number
            section (int) : section number

        Returns:
            controls (ndarray) :
                1-D array of controls

        """
        div_back, div_front = self._division_controls(control, section)
        return self.p[div_front:div_back] * self.unit_controls[section][control]

    def controls_all_section(self, control):
        """get controls array

        Args:
            control (int) : control number

        Returns:
            controls_all_section ((N, ) ndarray) :
                1-D array of all section control

        """
        temp = np.zeros(0)
        for i in range(self.number_of_section):
            temp = np.concatenate([temp, self.controls(control, i)])
        return temp

    def time_start(self, section):
        """ get time at section "start"

        Args:
            section (int) : section

        Returns:
            time_start (int) : time at section start

        """
        if (section == 0):
            return self.t0
        else:
            time_start_index = range(-self.number_of_section - 1, 0)
            return self.p[time_start_index[section]] * self.unit_time

    def time_final(self, section):
        """ get time at section "end"

        Args:
            section (int) : section

        Returns:
            time_final (int) : time at section end

        """
        time_final_index = range(-self.number_of_section, 0)
        return self.p[time_final_index[section]] * self.unit_time

    def time_final_all_section(self):
        """ get time at "end"

        Args:
            section (int) : section

        Returns:
            time_final_all_section (int) : time at end

        """
        tf = []
        for section in range(self.number_of_section):
            tf = tf + [self.time_final(section)]
        return tf

    def set_states(self, state, section, value):
        """set value to state at specific section

        Args:
            state (int) : state
            section (int) : section
            value (int) : value

        """
        assert len(value) == self.nodes[section], "Error: value length is NOT match nodes length"
        div_back, div_front = self._division_states(state, section)
        self.p[div_front:div_back] = value / self.unit_states[section][state]

    def set_states_all_section(self, state, value_all_section):
        """set value to state at all section

        Args:
            state (int) : state
            value_all_section (int) : value

        """
        div = 0
        for i in range(self.number_of_section):
            value = value_all_section[div:div + self.nodes[i]]
            div = div + self.nodes[i]
            self.set_states(state, i, value)

    def set_controls(self, control, section, value):
        """set value to control at all section

        Args:
            control (int) : control
            section (int) : section
            value (int) : value

        """
        assert len(value) == self.nodes[section], "Error: value length is NOT match nodes length"
        div_back, div_front = self._division_controls(control, section)
        self.p[div_front:div_back] = value / self.unit_controls[section][control]

    def set_controls_all_section(self, control, value_all_section):
        """set value to control at all section

        Args:
            control (int) : control
            value_all_section (int) : value

        """
        div = 0
        for i in range(self.number_of_section):
            value = value_all_section[div:div + self.nodes[i]]
            div = div + self.nodes[i]
            self.set_controls(control, i, value)

    def set_time_final(self, section, value):
        """ set value to final time at specific section

        Args:
            section (int) : seciton
            value (float) : value

        """
        time_final_index = range(-self.number_of_section, 0)
        self.p[time_final_index[section]] = value / self.unit_time

    def time_to_tau(self, time):
        time_init = min(time)
        time_final = max(time)
        time_center = (time_init + time_final) / 2
        temp = []
        for x in time:
            temp += [2 / (time_final - time_init) * (x - time_center)]
        return np.array(temp)

    def time_update(self):
        """ get time array after optimization

        Returns:
            time_update : (N,) ndarray
                time array

        """
        self.time = []
        t = [0] + self.time_final_all_section()
        for i in range(self.number_of_section):
            self.time.append((t[i+1] - t[i]) / 2.0 * self.tau[i]
                             + (t[i+1] + t[i]) / 2.0)
        return np.concatenate([i for i in self.time])

    def time_knots(self):
        """ get time at knot point

        Returns:
            time_knots (list) : time at knot point

        """
        return [0] + self.time_final_all_section()

    def index_states(self, state, section, index=None):
        """ get index of state at specific section

        Args:
            state (int) : state
            section (int) : section
            index (int, optional) : index

        Returns:
            index_states (int) : index of states
        """
        div_back, div_front = self._division_states(state, section)
        if (index is None):
            return div_front
        assert index < div_back - div_front, "Error, index out of range"
        if (index < 0):
            index = div_back - div_front + index
        return div_front + index

    def index_controls(self, control, section, index=None):
        div_back, div_front = self._division_controls(control, section)
        if (index is None):
            return div_front
        assert index < div_back - div_front, "Error, index out of range"
        if (index < 0):
            index = div_back - div_front + index
        return div_front + index

    def index_time_final(self, section):
        time_final_range = range(-self.number_of_section, 0)
        return self.number_of_variables + time_final_range[section]

    """
    ===========================
    UNIT SCALING ZONE
    ===========================
    """
    def set_unit_states(self, state, section, value):
        """ set a canonical unit value to the state at a specific section

        Args:
            state (int) : state
            section (int) : section
            value (float) : value

        """
        self.unit_states[section][state] = value

    def set_unit_states_all_section(self, state, value):
        """ set a canonical unit value to the state at all sections

        Args:
            state (int) : state
            value (float) : value

        """
        for i in range(self.number_of_section):
            self.set_unit_states(state, i, value)

    def set_unit_controls(self, control, section, value):
        """ set a canonical unit value to the control at a specific section

        Args:
            control (int) : control
            section (int) : section
            value (float) : value

        """
        self.unit_controls[section][control] = value

    def set_unit_controls_all_section(self, control, value):
        """ set a canonical unit value to the control at all sections

        Args:
            control (int) : control
            value (float) : value

        """
        for i in range(self.number_of_section):
            self.set_unit_controls(control, i, value)

    def set_unit_time(self, value):
        """ set a canonical unit value to the time

        Args:
            value (float) : value
        """
        self.unit_time = value
        time_init = np.array(self.time_init) / value
        self.time_init = list(time_init)
        self.time = []
        for index, node in enumerate(self.nodes):
            self.time.append((time_init[index+1] - time_init[index]) / 2.0 * self.tau[index]
                             + (time_init[index+1] + time_init[index]) / 2.0)
        self.t0 = time_init[0]
        self.time_all_section = np.concatenate([i for i in self.time])
        for section in range(self.number_of_section):
            self.set_time_final(section, time_init[section+1] * value)

    """ ==============================
    """

    def _dummy_func():
        pass

    """ ==============================
    """
    def solve(self, obj, display_func=_dummy_func, **options):
        """ solve NLP

        Args:
            obj (object instance) : instance
            display_func (function) : function to display intermediate values
            ftol (float, optional) : Precision goal for the value of f in the
                stopping criterion, (default: 1e-6)
            maxiter (int, optional) : Maximum number of iterations., (default : 25)

        Examples:
            "prob" is Problem class's instance.

            >>> prob.solve(obj, display_func, ftol=1e-12)

        """
        assert len(self.dynamics) != 0, "It must be set dynamics"
        assert self.cost is not None, "It must be set cost function"
        assert self.equality is not None, "It must be set equality function"
        assert self.inequality is not None, "It must be set inequality function"


        def equality_add(equality_func, obj):
            """ 
            Add pseudospectral method conditions to equality function.
            collocation point condition and knotting condition.
            """
            result = self.equality(self, obj)

            # collation point condition
            for i in range(self.number_of_section):
                D = self.D
                derivative = np.zeros(0)
                for j in range(self.number_of_states[i]):
                    state_temp = self.states(j, i) / self.unit_states[i][j]
                    derivative = np.hstack((derivative, D[i].dot(state_temp)))
                tix = self.time_start(i) / self.unit_time
                tfx = self.time_final(i) / self.unit_time
                dx = self.dynamics[i](self, obj, i)
                result = np.hstack((result, derivative - (tfx - tix) / 2.0 * dx))
                
            # knotting condition
            for knot in range(self.number_of_section - 1):
                if (self.number_of_states[knot] != self.number_of_states[knot + 1]):
                    continue  # if states are not continuous on knot, knotting condition skip
                for state in range(self.number_of_states[knot]):
                    param_prev = self.states(state, knot) / self.unit_states[knot][state]
                    param_post = self.states(state, knot + 1) / self.unit_states[knot][state]
                    if (self.knot_states_smooth[knot]):
                        result = np.hstack((result, param_prev[-1] - param_post[0]))

            return result
        
        
        
        
        def cost_add(cost_func, obj):
            """
            Combining nonintegrated function and integrated function.
            """
            not_integrated = self.cost(self, obj)
            if self.running_cost is None:
                return not_integrated
            integrand = self.running_cost(self, obj)
            weight = np.concatenate([i for i in self.w])
#            integrated = sum(integrand * weight)   # old version         
            integrated=0.0
            for i in range(self.number_of_section):
                tix = self.time_start(i) / self.unit_time
                tfx = self.time_final(i) / self.unit_time            
                integrated += (tfx - tix)/2.0 * sum(integrand * weight)            
            return not_integrated + integrated

        def wrap_for_solver(func, arg0, arg1):
            def for_solver(p, arg0, arg1):
                self.p = p                            
                return func(arg0, arg1)
            return for_solver

        def jacobian_cost(x, self, obj):
            jacobi = grad(wrap_for_solver(cost_add, self.cost, obj))
            jacobian_x = jacobi(x, self.cost, obj)
            return jacobian_x

#        def jacobian_equality(x, self, obj):            
#            jacobi = jacobian(wrap_for_solver(equality_add, self.equality, obj))
#            jacobian_x = jacobi(x, self.equality, obj)
#            return jacobian_x

#        def jacobian_inequality(x, self, obj):            
#            jacobi = jacobian(wrap_for_solver(self.inequality, self, obj))
#            jacobian_x = jacobi(x, self, obj)
#            return jacobian_x
        
        cons = ({'type': 'eq',
                 'fun': wrap_for_solver(equality_add, self.equality, obj),
#                 'jac': jacobian_equality,
                 'args': (self, obj,)},
                {'type': 'ineq',
                 'fun': wrap_for_solver(self.inequality, self, obj),
#                 'jac': jacobian_inequality,                 
                 'args': (self, obj,)})
           

        def callback_fun(x):
            self.compteur += 1
            cost_fun_= wrap_for_solver(cost_add, self.cost, obj)
            cost_now = cost_fun_(x, self.cost, obj)
            self.J += [cost_now]  # total cost
            print('Current cost =',cost_now,' | iteration=', self.compteur)
        
        ftol = options.setdefault("ftol", 1e-6)
        maxiter = options.setdefault("maxiter", 25)

        while self.iterator < self.maxIterator:
            print("---- iteration : {0} ----".format(self.iterator+1))
            opt = optimize.minimize(wrap_for_solver(cost_add, self.cost, obj),
                                    self.p,
                                    args=(self, obj),
                                    constraints=cons,
                                    jac=jacobian_cost,
                                    callback=callback_fun,
                                    method='SLSQP',                                    
                                    options={"disp": True,
                                             "maxiter": maxiter,
                                             "ftol": ftol})
            print(opt.message)
            display_func()
            print("")
            if not(opt.status):
                break
            self.iterator += 1
        
        return opt
    """ ==============================
    """

    # return cost history during optimisation
    def cost_history(self):
        return self.J

    # return LGL weights
    def LGL_weights(self):
        return self.w
    
    # initialization
    def __init__(self, time_init, nodes, number_of_states, number_of_controls,
                 maxIterator = 100, method="LGL"):
        assert isinstance(time_init, list), \
            "error: time_init is not list"
        assert isinstance(nodes, list), \
            "error: nodes are not list"
        assert isinstance(number_of_states, list), \
            "error: number of states are not list"
        assert isinstance(number_of_controls, list), \
            "error: number of controls are not list"
        assert len(time_init) == len(nodes) + 1, \
            "error: time_init length is not match nodes length"
        assert len(nodes) == len(number_of_states), \
            "error: nodes length is not match states length"
        assert len(nodes) == len(number_of_controls), \
            "error: nodes length is not match controls length"
            
        self.compteur=0
        self.J=[]
        
        self.nodes = nodes
        self.number_of_states = number_of_states
        self.number_of_controls = number_of_controls
        self.div = self._make_param_division(nodes, number_of_states, number_of_controls)
        self.number_of_section = len(self.nodes)
        self.number_of_param = np.array(number_of_states) + np.array(number_of_controls)
        self.number_of_variables = sum(self.number_of_param * nodes) + self.number_of_section
        self.tau = []
        self.w = []
        self.D = []
        self.time = []
        
        for index, node in enumerate(nodes):
            self.tau.append(self._nodes_LGL(node))
            self.w.append(self._weight_LGL(node))
            self.D.append(self._differentiation_matrix_LGL(node))
            self.time.append((time_init[index+1] - time_init[index]) / 2.0 * self.tau[index]
                             + (time_init[index+1] + time_init[index]) / 2.0)
        self.maxIterator = maxIterator
        self.iterator = 0
        self.time_init = time_init
        self.t0 = time_init[0]
        self.time_all_section = np.concatenate([i for i in self.time])
        
        # ====
        self.unit_states = []
        self.unit_controls = []
        self.unit_time = 1.0
        for i in range(self.number_of_section):
            self.unit_states.append([1.0]*self.number_of_states[i])
            self.unit_controls.append([1.0]*self.number_of_controls[i])
            
        # ====
        self.p = np.zeros(self.number_of_variables, dtype=float)
        
        # ====
        # function
        self.dynamics = []
        self.knot_states_smooth = []
        self.cost = None
        self.running_cost = None
        self.cost_derivative = None
        self.equality = None
        self.inequality = None
        
        # ====
        for section in range(self.number_of_section):
            self.set_time_final(section, time_init[section+1])
            self.dynamics.append(None)
        for section in range(self.number_of_section-1):
            self.knot_states_smooth.append(True)

    def __repr__(self):
        s = "---- parameter ----" + "\n"
        s += "nodes = " + str(self.nodes) + "\n"
        s += "number of states    = " + str(self.number_of_states) + "\n"
        s += "number of controls  = " + str(self.number_of_controls) + "\n"
        s += "number of sections  = " + str(self.number_of_section) + "\n"
        s += "number of variables = " + str(self.number_of_variables) + "\n"
        s += "---- algorithm ----" + "\n"
        s += "max iteration = " + str(self.maxIterator) + "\n"
        s += "---- function  ----" + "\n"
        s += "dynamics        = " + str(self.dynamics) + "\n"
        s += "cost            = " + str(self.cost) + "\n"
        s += "cost_derivative = " + str(self.cost_derivative) + "\n"
        s += "equality        = " + str(self.equality) + "\n"
        s += "inequality      = " + str(self.inequality) + "\n"
        s += "knot_states_smooth = " + str(self.dynamics) + "\n"

        return s

    def to_csv(self, statename, controlname,
               filename="Optimisation_output.csv", delimiter=","):
        """ 
        output states, controls and time to csv file

        Args:
            filename (str, optional) : csv filename
            delimiter : (str, optional) : default ","

        """

        header = "time, "
        result = np.zeros(0)
        result = np.hstack((result, self.time_update()))
        
        for i in range(self.number_of_states[0]):
            header += statename[i] + ','
            result = np.vstack((result, self.states_all_section(i)))
            
        for i in range(self.number_of_controls[0]):
            header += controlname[i] + ','
            result = np.vstack((result, self.controls_all_section(i)))
            
        np.savetxt(filename, result.T, delimiter=delimiter, header=header)
        print("Completed saving \"%s\"" % (filename))


    def plot(self, title_comment=""):
        """ plot inner variables that to be optimized

        Args:
            title_comment (str) : string for title

        """
        plt.figure()
        plt.title("OpenGoddard inner variables" + title_comment)
        plt.plot(self.p, "o")
        plt.xlabel("variables")
        plt.ylabel("value")
        for section in range(self.number_of_section):
            for line in self.div[section]:
                plt.axvline(line, color="C%d" % ((section+1) % 6), alpha=0.5)
        plt.grid()

    def method_LGL(self, n):
        """ 
        Legendre-Gauss-Lobatto Pseudospectral method
        Gauss-Lobatto nodes are roots of :math: "P'_{n-1}(x)" and -1, 1.

        Args:
            n (int) : number of nodes

        Returns:
            ndarray, ndarray, ndarray : nodes, weight, differentiation_matrix

        References:
            Fariba Fahroo and I. Michael Ross. "Advances in Pseudospectral Methods
            for Optimal Control", AIAA Guidance, Navigation and Control Conference
            and Exhibit, Guidance, Navigation, and Control and Co-located Conferences
            http://dx.doi.org/10.2514/6.2008-7309
        """
        
        nodes = self._nodes_LGL(n)
        weight = self._weight_LGL(n)
        D = self._differentiation_matrix_LGL(n)  
        return nodes, weight, D
    



## Initial Guess Class of optimisation variables
class Guess:
    """
    Collection of class methods (for initial value guess for optimization)
    """

    @classmethod
    def constant(cls, time, const):
        """ return constant values that array size is same as time length

        Args:
            time (array_like) :
            const (float) : set value

        Returns:
            (N, ) ndarray

        """
        return np.ones(len(time)) * const

    @classmethod
    def linear(cls, time, y0, yf):
        """ return linear function values that array size is same as time length

        Args:
            time (array_like) : time
            y0 (float): initial value
            yf (float): final value

        Returns:
            (N, ) ndarray

        """
        x = np.array([time[0], time[-1]])
        y = np.array([y0, yf])
        f = interpolate.interp1d(x, y)
        return f(time)

    @classmethod
    def cubic(cls, time, y0, yprime0, yf, yprimef):
        """ return cubic function values that array size is same as time length

        Args:
            time (array_like) : time
            y0 (float) : initial value
            yprime0 (float) : slope of initial value
            yf (float) : final value
            yprimef (float) : slope of final value

        Returns:
            (N, ) ndarray

        """
        y = np.array([y0, yprime0, yf, yprimef])
        t0 = time[0]
        tf = time[-1]
        A = np.array([[1, t0, t0**2, t0**3], [0, 1, 2*t0, 3*t0**2],
                      [1, tf, tf**2, tf**3], [0, 1, 2*tf, 3*tf**2]])
        invA = np.linalg.inv(A)
        C = invA.dot(y)
        ys = C[0] + C[1]*time + C[2]*time**2 + C[3]*time**3
        return ys

    @classmethod
    def interpolation(cls, time, y, time_interp):
        """ return interpolated values that array size is same as time_interp length

        Args:
            time (array_like) : time
            y (array_like) : variable value

        Returns:
            (N, ) ndarray
        """
        f = interp.interp1d(time, y, fill_value="extrapolate")
        y_interp = f(time_interp)
        return y_interp
    
    

## Condition class for assembling equality/inequality conditions
class Condition(object):
    """ Condition class
    
    Examples:
        for examples in equality function.

        Initial condtion : x[0] = 0.0
        Termination Condition : x[-1] = 100

        >>> result = Condition()
        >>> result.equal(x[0], 0.0)
        >>> result.equal(x[-1], 100)
        >>> return result()

        for examples in inequality function

        Inequation condtion : 0.0 <= x <= 100

        >>> result = Condition()
        >>> result.lower_bound(x, 0.0)
        >>> result.upper_bound(x, 100)
        >>> return result()

    """
    def __init__(self, length=0):
        self._condition = np.zeros(length)


    def add(self, arg, unit=1.0):
        """add condition
        Args:
            arg (array_like) : condition
        """
        self._condition = np.hstack((self._condition, arg / unit))


    def equal(self, arg1, arg2, unit=1.0):
        """add equation constraint condition in Problem equality function

        arg1 = arg2

        Args:
            arg1 (float or array_like) : right side of the equation
            arg2 (float or array_like) : left side of the equation
            unit (float, optional) : argX / unit (default : 1.0)

        Notes:
            It must be used in equality function.
        """
        arg = arg1 - arg2
        self.add(arg, unit)


    def lower_bound(self, arg1, arg2, unit=1.0):
        """add inequation constraint condition in Problem inequality function

        arg1 >= arg2

        Args:
            arg1 (array like) : arg1 is greater than or equal to arg2
            arg2 (float or array like) : arg1 is greater than or equal to arg2
            unit (float, optional) : argX / unit (default : 1.0)

        Notes:
            It must be used in inequality function.
        """
        arg = arg1 - arg2
        self.add(arg, unit)


    def upper_bound(self, arg1, arg2, unit=1.0):
        """add inequation constraint condition in Problem inequality function

        arg1 <= arg2

        Args:
            arg1 (array like) : arg1 is less than or equal to arg2
            arg2 (float or array like) : arg1 is less than or equal to arg2
            unit (float, optional) : argX / unit (default : 1.0)

        Notes:
            It must be used in inequality function.
        """
        arg = arg2 - arg1
        self.add(arg, unit)

    def change_value(self, index, value):
        self._condition[index] = value


    def __call__(self):
        return self._condition


## Dynamics class
class Dynamics(object):
    """ Dynamics class.

    Thin wrapper for dynamics function. Behave like a dictionary type.

    Examples:
        Dynamics class must be used in dynamics function.
        It is an example of the equation of motion of thrust and free fall.
        Thrust is controllable.

        .. math::

            \dot{x} &= v
            \dot{v} &= T/m - g

        >>> def dynamics(prob, obj, section):
        >>>     x = prob.states(0, section)
        >>>     v = prob.states(1, section)
        >>>     T = prob.controls(0, section)
        >>>     g = 9.8
        >>>     m = 1.0
        >>>     dx = Dynamics(prob, section)
        >>>     dx[0] = v
        >>>     dx[1] = T / m - g
        >>>     return dx()
    """

    def __init__(self, prob, section=0):
        """ prob is instance of OpenGoddard class
        """
        self.section = section
        self.number_of_state = prob.number_of_states[section]
        self.unit_states = prob.unit_states
        self.unit_time = prob.unit_time
        for i in range(self.number_of_state):
            self.__dict__[i] = np.zeros(prob.nodes[section])

    def __getitem__(self, key):
        assert key < self.number_of_state, "Error, Dynamics key out of range"
        return self.__dict__[key]

    def __setitem__(self, key, value):
        assert key < self.number_of_state, "Error, Dynamics key out of range"
        self.__dict__[key] = value

    def __call__(self):
        dx = np.zeros(0)
        for i in range(self.number_of_state):
            temp = self.__dict__[i] * (self.unit_time / self.unit_states[self.section][i])
            dx = np.hstack((dx, temp))
        return dx



