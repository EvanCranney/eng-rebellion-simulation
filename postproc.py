"""
Generates the following statistics:

Over all timesteps in a run:
    (1) COUNT_ACTIVE = average number of active agents
    (2) COUNT_INACTIVE = average number of inactive agents
    (3) COUNT_JAILED = average number of jaile agents

Over all rebellions in a run:
    (1) REBELLION_DURATION = how many periods a rebellion lasted for
    (2) REBELLION_FREQUENCY = number of timesteps between starts
    (3) REBELLION_ACTIVE_AVG = avg number of active agents over rebellion
    (4) REBELLION_ACTIVE_MAX = max number of active agents over rebellion

Generates the following graphs:
    (1) Histogram of REBELLION_DURATION
    (2) Histogram of REBELLION_FREQUENCY
    (3) Histogram of REBELLION_ACTIVE_AVG
    (4) Histogram of REBELLION_ACTIVE_MAX
    (5) Time series of COUNT_ACTIVE

    Prefix
    _nj: To identify NetLogo doc related attributes
    _j: To identify Java doc related attributes

    Experiments: Punctuated Equilibrium
    movement off ~ on experiments
    01 ~ 06
    02 ~ 07
    03 ~ 08
    04 ~ 09
    05 ~ 10

    Quiescence
    11
    12
    13

    Anarchy
    14
    15
"""

import scipy
import pandas as pd
import matplotlib.mlab as mlab
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

REBELLION_THRESHOLD = 50  # num active agents to constitue a rebellion
NETLOGO_DATA_FILE_NAME = "experiments/1_raw_data/Rebellion experiment 15 (NetLogo).csv"
JAVA_DATA_FILE_NAME = "experiments/1_raw_data/Rebellion experiment 15 (Java).csv"

# read in the excel file
print("Reading NetLogo file " + NETLOGO_DATA_FILE_NAME)
df_nl = pd.read_csv(NETLOGO_DATA_FILE_NAME, skiprows=range(0, 21))
df_nl.drop(df_nl.iloc[:, 0:1], inplace=True, axis=1)
df_nl.drop(df_nl.iloc[:, 4:8], inplace=True, axis=1)
print("Reading Java file " + JAVA_DATA_FILE_NAME)
df_j = pd.read_csv(JAVA_DATA_FILE_NAME, skiprows=range(0, 21))
df_j.drop(df_j.iloc[:, 0:1], inplace=True, axis=1)
df_j.drop(df_j.iloc[:, 4:8], inplace=True, axis=1)

df_nl_header = pd.read_csv(NETLOGO_DATA_FILE_NAME, skiprows=range(0, 7), nrows=6)
df_nl_header.drop(df_nl_header.iloc[:, 2:5], inplace=True, axis=1)
df_j_header = pd.read_csv(JAVA_DATA_FILE_NAME, skiprows=range(0, 7), nrows=6)
df_j_header.drop(df_j_header.iloc[:, 2:5], inplace=True, axis=1)

print()
print("======================================")
print("Summary statistics over all timesteps (NetLogo):")
print(df_nl.mean(axis=0))
print(df_nl_header)
print()
print("Summary statistics over all timesteps (Java):")
print(df_j.mean(axis=0))
print(df_j_header)
print("======================================")
print()

df_nl["IS_REBELLION"] = df_nl["ACTIVE"].apply(lambda x: x >= REBELLION_THRESHOLD)

df_j["IS_REBELLION"] = df_j["ACTIVE"].apply(lambda x: x >= REBELLION_THRESHOLD)


def find_rebellions(df):
    rebellions = []
    i = 0
    rebellion = []
    while i < df.shape[0]:
        if not df.at[i, "IS_REBELLION"]:
            if rebellion != []:
                rebellions.append((i - len(rebellion), rebellion))
            rebellion = []
        else:
            rebellion.append(df.at[i, "ACTIVE"])
        i += 1
    return rebellions
    # print(rebellions)


def find_rebellion_duration(rebellions):
    rebellion_duration = []
    for t, actives in rebellions:
        rebellion_duration.append(len(actives))
    print("REBELLION_DURATION ", rebellion_duration)
    # print("Average of REBELLION_DURATION: ", reduce(lambda x, y: x + y, rebellion_duration) / (1.0 *
    #                                                                                            len(rebellion_duration)))
    print()
    return rebellion_duration


def find_rebellion_frequency(rebellions):
    rebellion_frequency = []
    for i in range(1, len(rebellions)):
        rebellion_frequency.append(rebellions[i][0] - rebellions[i - 1][0])
    print("REBELLION_FREQUENCY ", rebellion_frequency)
    # print("Average of REBELLION_FREQUENCY: ",
    #       reduce(lambda x, y: x + y, rebellion_frequency) / (1.0 * len(rebellion_frequency)))
    print()
    return rebellion_frequency


def find_rebellion_active_average(rebellions):
    rebellion_active_average = []
    for t, actives in rebellions:
        rebellion_active_average.append(reduce(lambda x, y: x + y, actives) / (1.0 * len(actives)))
    print("REBELLION_ACTIVE_AVG", rebellion_active_average)
    # print("Average of REBELLION_ACTIVE_AVG: ",
    #       reduce(lambda x, y: x + y, rebellion_active_average) / (1.0 * len(rebellion_active_average)))
    print()
    return rebellion_active_average


def find_rebellion_active_max(rebellions):
    rebellion_active_max = []
    for t, actives in rebellions:
        rebellion_active_max.append(max(actives))
    print("REBELLION_ACTIVE_MAX", rebellion_active_max)
    # print("Average of REBELLION_ACTIVE_MAX: ",
    #       reduce(lambda x, y: x + y, rebellion_active_max) / (1.0 * len(rebellion_active_max)))
    print()
    return rebellion_active_max


def draw_diagrams(df, rebellion_duration, rebellion_frequency, rebellion_active_average, rebellion_active_max,
                  from_model):
    n_bins = 12

    fig = plt.figure()
    # fig, axes = plt.subplots(nrows=2, ncols=2)
    # fig.suptitle(from_model, fontsize=10)
    # ax0, ax1, ax2, ax3 = axes.flatten()
    x = rebellion_duration
    ax0 = fig.add_subplot(111)
    ax0.hist(x, n_bins, normed=1, histtype='bar', color='black')
    ax0.legend(prop={'size': 10})
    ax0_title = from_model + ', R_DUR Distribution'
    ax0.set_title(ax0_title, fontsize=20)
    ax0.set_xlabel("Rebellion Frequency", fontsize=20)
    ax0.set_ylabel("Relative Frequency", fontsize=20)
    ax0.set_ylim([0, 0.6])

    fig = plt.figure()
    x = rebellion_frequency
    ax1 = fig.add_subplot(111)
    ax1.hist(x, n_bins, normed=1, histtype='bar', color='black')
    ax1.legend(prop={'size': 10})
    ax1_title = from_model + ', R_FREQ Distribution'
    ax1.set_title(ax1_title, fontsize=20)
    ax1.set_xlabel("Rebellion Frequency", fontsize=20)
    ax1.set_ylabel("Relative Frequency", fontsize=20)
    ax1.set_ylim([0, 0.06])
    # fig.text(.15, .05, df_nl_header)

    fig = plt.figure()
    x = rebellion_active_average
    ax2 = fig.add_subplot(111)
    ax2.hist(x, n_bins, normed=1, histtype='bar', color='black')
    ax2.legend(prop={'size': 10})
    ax2_title = from_model + ', R_AVG Distribution'
    ax2.set_title(ax2_title, fontsize=20)
    ax2.set_xlabel("Rebellion Frequency", fontsize=20)
    ax2.set_ylabel("Rebellion Average Size", fontsize=20)
    ax2.set_ylim([0, 0.014])

    fig = plt.figure()
    x = rebellion_active_max
    ax3 = fig.add_subplot(111)
    ax3.hist(x, n_bins, normed=1, histtype='bar', color='black')
    ax3.legend(prop={'size': 10})
    ax3_title = from_model + ', R_MAX Distribution'
    ax3.set_title(ax3_title, fontsize=20)
    ax3.set_xlabel("Rebellion Frequency", fontsize=20)
    ax3.set_ylabel("Relative Frequency", fontsize=20)
    ax3.set_ylim([0, 0.0045])

    fig = plt.figure()
    time = range(0, 500)
    active_agents = df["ACTIVE"][0:500]
    ax5 = fig.add_subplot(111)
    ax5.plot(time, active_agents)
    ax5_title = from_model + ', N_ACTIVE Time Series'
    ax5.set_title(ax5_title, fontsize=20)
    ax5.set_xlabel("Time-Step", fontsize=20)
    ax5.set_ylabel("Number of Active Agents", fontsize=20)
    ax5.set_ylim(0, 200)

    fig.tight_layout()
    plt.show()


rebellions_nl = find_rebellions(df_nl)
rebellions_j = find_rebellions(df_j)
print("==================NetLogo=============")

rebellion_duration_nl = find_rebellion_duration(rebellions_nl)
rebellion_frequency_nl = find_rebellion_frequency(rebellions_nl)
rebellion_active_average_nl = find_rebellion_active_average(rebellions_nl)
rebellion_active_max_nl = find_rebellion_active_max(rebellions_nl)
print("=====================Java=============")

rebellion_duration_j = find_rebellion_duration(rebellions_j)
rebellion_frequency_j = find_rebellion_frequency(rebellions_j)
rebellion_active_average_j = find_rebellion_active_average(rebellions_j)
rebellion_active_max_j = find_rebellion_active_max(rebellions_j)
print("======================================")

draw_diagrams(df_nl, rebellion_duration_nl, rebellion_frequency_nl, rebellion_active_average_nl, rebellion_active_max_nl
               , "NetLogo")
draw_diagrams(df_j, rebellion_duration_j, rebellion_frequency_j, rebellion_active_average_j, rebellion_active_max_j
              , "Java")
