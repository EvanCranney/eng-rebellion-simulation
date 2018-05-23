# Rebellion Simulation

Java implementation of the NetLogo Rebellion model. Based on Epstein 2002's
"Modeling Civil Violence: An Agent-Based Computational Approach".

## System Architecture

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
