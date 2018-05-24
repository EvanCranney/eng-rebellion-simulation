# Rebellion Simulation

Java implementation of the NetLogo Rebellion model. Based on Epstein 2002's
"Modeling Civil Violence: An Agent-Based Computational Approach".

## Contributers


| Name                              |      Email                            |Student  
|-----------------------------------|:-------------------------------------:|------------------:|
| Evan Cranney                      |  e.cranney@student.unimelb.edu.au     | 584281          |
| Shalitha Weerakoon Karunatilleke  |  sweerakoon@student.unimelb.edu.au   	| 822379        |

## Guide  
Change the following parameters from the Rebellion.java class to run the 
experiments (This part is similar to the NetLogo Rebellion model)
``` 
    GOVERNMENT_LEGITIMACY = 0.0;
    MOVEMENT_ON = true;
    AGENT_DENSITY = 0.70;
    VISION = 7.0;
    MAX_JAIL_TERM = 1;
    COP_DENSITY = 0.00;
``` 
Following parameter for the filename will create a CSV file in the project 
root (Output of this file is similar to NetLogo BehaviourSpace CSV output)

``` 
    OUTFILE = "metrics.csv";
``` 

Following parameter is to change the number of steps to run the simulation 
``` 
    STEPS = 200;
``` 

## Extended Features
#### EXT 1: Iteration Model
Change the following parameters to run the model
ex: In this model it will start to decrement the government legitimacy by 0.005 
since the 
begining to  
``` 
    simulation = SimulationType.ITERATIVE;
    EXT1_LEGITIMACY_DECREMENT = 0.005;
``` 

#### EXT 2: Single Run Model
Change the following parameters to run the model.
ex: In following example it will run the model for 50 ticks and then it will 
decrement the government legitimacy to 0.30
``` 
    EXT2_LEGITIMACY_DECREMENT = 0.30;
    EXT2_DECREMENT_TIMESTEP = 50;
``` 

## Chart Generation
Run the following script to generate charts for each experiments by changing 
the following paraters from postproc.py file

``` 
    NETLOGO_DATA_FILE_NAME = "{Include BehaviourSpace CSV file}"
    JAVA_DATA_FILE_NAME = "metrics.csv"
``` 

## System Architecture
``` 
    Grid
        grid : Cell[][]
    
    Cell
        enter()
        leave()
    
    Neighborhood
        neighborhood : Cell[]
        cops : Cop[]
        agents : Agent[]
        enter()
        leave()
        isEmpty()
        getRandomCell()
        getNumCops()
        getNumActiveAgents()
        
    Person
        move()
        getLocation()
    
    Agent : Person
        move()
        rebel()
        arrest()
        passJailTime()
        isJailed()
        isActive()
    
    Cop : Person
        move()
        enforce()
    
    Rebellion
        instantiateGrid(width, height)
        instantiateAgents(n)
        intantiateCops(n)
        tick()
            -- agents move
            -- rebels rebel
            -- cops enforce
``` 

