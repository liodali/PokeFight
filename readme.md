# PokemonGoApp
> Android application build it  with kotlin that find and collect pokemons

## Build
* should use Android Studio 2020.3.1 canary 13 or above (beta 1 or 2)



##### In this project, we implement the  clean architecture
* we have 3 layer:

  * <srong>App module </string>  : This module contains all of the code related to the UI/Presentation layer such as activities,fragment,dialog,custom views  and contain viewmodel,dependency injection module app
  * <srong>Core</string> : holds all concrete implementations of our repositories,usecaes and other data sources like  network
  * <srong>Domain module </string>  : contain all interfaces of repositories ,usecase and data classes



> I used hilt as dependency injection for this project
> I used retorfit for http calls and flowAPI to collect data
> I used osm map to show random location of pokemon

### screenshots
<img width="150" height="300" src="/screenshot/explore_ui.png" >
<img width="150" height="300" src="/screenshot/community_ui.png" alt="game was terminated">
<img width="150" height="300" src="/screenshot/my_team_ui.png" alt="game was terminated">
<img width="150" height="300" src="/screenshot/captured_ui.png" alt="game was terminated">
<img width="150" height="300" src="/screenshot/detail_my_poke.png" alt="game was terminated">
<img width="150" height="300" src="/screenshot/detail_other_poke.png" alt="game was terminated">
<img width="150" height="300" src="/screenshot/detail_poke.png" alt="game was terminated">
<img width="150" height="300" src="/screenshot/dialog_to_add_poke.png" alt="game was terminated">
<img width="150" height="300" src="/screenshot/poke_added.png" alt="game was terminated">
