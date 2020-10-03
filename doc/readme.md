# Mobile Application Development: Assignment 2

## Activities
    - TitleAct
    - SettingsAct
    - MapAct
    - DetailsAct
    
## Activity: TitleAct
This is the default and initial loaded activity. Displays:
    - Application name
    - Author name
    - Action: Launch SettingsAct
    - Action: Launch MapAct
    
## Activity: MapAct
This is the core part of the application and splits functionality between the activity and 
embedded fragments. The activity will display information that overlays the map fragment. The map
will exist in a fragment. Below the map another fragment will display a selection of structures
that can be added to the map. In portrait mode, another fragment will exist above the map with
editing tools. In landscape mode, this fragment will exist on the left of the map. On large 
devices it will float on the map fragment.

### Activity
Displays the following:
    - Game time
    - Money [+/-]{change}
    - Population
    - Employment Rate
    - Name of City
    - Temperature
    - Selected Tool
    
### MapFrag

### StructureFrag

### ToolsFrag
Displays a toolset:
    - Clear: Clears current tool
    - Details: Sets tool to details
    - Destruct: Sets tool to destruct