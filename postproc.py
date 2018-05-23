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
import numpy as np
# import either matplotlib or seaborn for graphs

REBELLION_THRESHOLD = 20 # num active agents to constitue a rebellion
DATA_FILE_NAME = "sample.csv"

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
for val in df["IS_REBELLION"]:
    print(val)
print(df.head())



