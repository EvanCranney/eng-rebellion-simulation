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
"""

import pandas as pd
import matplotlib.mlab as mlab
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

REBELLION_THRESHOLD = 50 # num active agents to constitue a rebellion
DATA_FILE_NAME = "experiments/3_processed_csv/Rebellion experiment 06 (NetLogo).csv"

# read in the excel file
print("Reading file " + DATA_FILE_NAME)
df = pd.read_csv(DATA_FILE_NAME)

print()
print("======================================")
print("Summary statistics over all timesteps:")
print(df.mean(axis=0))
print("======================================")
print()


print()
print("Summary statistics over rebellions")
df["IS_REBELLION"] = df["ACTIVE"].apply(lambda x: x >= REBELLION_THRESHOLD)
rebellions = []

i = 0
rebellion = []
while i < df.shape[0]:
    if not df.at[i, "IS_REBELLION"]:
        if rebellion != []:
            rebellions.append((i-len(rebellion),rebellion))
        rebellion = []
    else:
        rebellion.append(df.at[i, "ACTIVE"])
    i += 1
# print(rebellions)

rebellion_duration = []
for t, actives in rebellions:
    rebellion_duration.append(len(actives))
print("REBELLION_DURATION: ", rebellion_duration)

rebellion_frequency = []
for i in range(1,len(rebellions)):
    rebellion_frequency.append(rebellions[i][0] - rebellions[i-1][0])
print("REBELLION_FREQUENCY: ", rebellion_frequency)

rebellion_active_average = []
for t, actives in rebellions:
    rebellion_active_average.append(reduce(lambda x, y: x + y, actives) / (1.0*len(actives)))
print("REBELLION_ACTIVE_AVG: ", rebellion_active_average)

rebellion_active_max = []
for t, actives in rebellions:
    rebellion_active_max.append(max(actives))
print("REBELLION_ACTIVE_MAX: ", rebellion_active_max)
print()
print("Average of REBELLION_ACTIVE_MAX: ", reduce(lambda x, y: x + y, rebellion_active_max) / (1.0*len(rebellion_active_max)))
print("Average of REBELLION_ACTIVE_AVG: ", reduce(lambda x, y: x + y, rebellion_active_average) / (1.0*len(rebellion_active_average)))
print("Average of REBELLION_FREQUENCY: ", reduce(lambda x, y: x + y, rebellion_frequency) / (1.0*len(rebellion_frequency)))
print("Average of REBELLION_DURATION: ", reduce(lambda x, y: x + y, rebellion_duration) / (1.0*len(rebellion_duration)))

np.random.seed(0)

# n_bins = 12
# x = rebellion_duration

# fig, axes = plt.subplots(nrows=2, ncols=2)
# ax0, ax1, ax2, ax3 = axes.flatten()

# ax0.hist(x, n_bins, normed=1, histtype='bar', color='black')
# ax0.legend(prop={'size': 10})
# ax0.set_title('R_DUR')
# ax0.set_ylim([0, 1])
#
# x = rebellion_frequency
#
# ax1.hist(x, n_bins, normed=1, histtype='bar', color='black')
# ax1.legend(prop={'size': 10})
# ax1.set_title('R_FREQ')
# ax1.set_ylim([0, 0.06])

# x = rebellion_active_average
#
# ax2.hist(x, n_bins, normed=1, histtype='bar', color='black')
# ax2.legend(prop={'size': 10})
# ax2.set_title(' R_AVG')
# ax2.set_ylim([0, 0.02])
#
# x = rebellion_active_max
#
# ax3.hist(x, n_bins, normed=1, histtype='bar', color='black')
# ax3.legend(prop={'size': 10})
# ax3.set_title('R_MAX')
# ax3.set_ylim([0, 0.013])


time = range(0, 500)
active_agents = df["ACTIVE"][0:500]

fig = plt.figure()
ax0 = fig.add_subplot(111)

line = ax0.plot(time, active_agents)
ax0.set_title('N_ACTIVE')
ax0.set_ylim(0, 400)

fig.tight_layout()
plt.show()
