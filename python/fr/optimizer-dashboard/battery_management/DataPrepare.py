"""
Final modification: 04/02/2019

@author: zhuj
"""

import numpy as np


def PrepareData(data):
    Em = np.double(data['em'])  # [kWh]
    Ep = np.double(data['ep'])  # [kWh]
    Pcp = np.double(data['pcp'])  # [kW]
    Pdp = np.double(data['pdp'])  # [kW]
    vec_E_Plim = np.double(data['vecEPlim'])  #
    T = np.round(np.double(data['t']) / 60.0)  # [h]
    rho_d = np.double(data['rhoD'][0])
    Epm = np.double(data['epm'])  # [kWh]
    Epp = np.double(data['epp'])  # [kWh]
    Ps = np.double(data['ps'])  # [kW]
    E0 = np.double(data['e0'])
    Etf = np.double(data['etf'])
    DT = np.double(data['dt'])
    try:
        rho_c = np.double(data['rhoC'][0])
        alpha = np.double(data['alphaPeak'][0])  # [euro/kWh]
    except:
        rho_c = rho_d
        alpha = 0.  # [euro/kWh]

    Date = []
    PPV = []
    Conso = []
    prix_achat = []
    prix_vente = []
    for item in data['dataset']:
        Date.append(item['date'])
        PPV.append(np.double(item['pProdGlobal']))  # [kW]
        Conso.append(np.double(item['pConsoGlobal']))  # [kW]
        prix_achat.append(np.double(item['prixAchat']))  # [euro/kWh]
        prix_vente.append(np.double(item['prixVente']))  # [euro/kWh]


    return Em, Ep, E0, Etf, Pcp, Pdp, vec_E_Plim, T, rho_c, rho_d, alpha, Conso, PPV, prix_achat, \
           prix_vente, Epm, Epp, Ps, alpha, Date, DT
