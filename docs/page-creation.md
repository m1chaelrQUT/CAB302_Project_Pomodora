# Page Creation

This document will outline how to:
- Create new pages
- Modify existing pages

Such that they align with the Controller and ThemeManagement paradigms of the project

# Page Creation
## Steps to success
1. When creating a new page, ensure that it has the following: 
   1. A root stackpane, with the assigned styleClass `.primitives`
   2. A second root stackpane, nested in the first, with the assigned styleClass `.theme`
   3. A third and final root stackpane, with the styleClass assigned `window-background` - This is your content pane, please also ensure that it has the assigned fx:id `contentPane`
2. When creating a page, if you're using the SceneBuilder tool, feel free to apply the base stylesheet so that you can get a better visual idea of the default look of the page. <br> `!! Make sure to remove the stylesheets from the FXML file when complete - failure to do so causes issues with theme management!!`
3. It is recommended to have the main content of your window nested in a further fourth container, as this will make it easy to add overlays later  via stackpanes placed invisibly over the main container (see the construction of [signin.fxml](../src/main/resources/com/qut/cab302_project_pomodora/fxml/signin.fxml))
4. When creating your page, you might realise you need a new styleClass. You can easily create a styleClass by adding it to the [base stylesheet](../src/main/resources/com/qut/cab302_project_pomodora/css/common/base.css). 

See more info on managing the base stylesheet and themes [here](theme-management.md)

# Page Modification
Page modification is rather simple, so long as you ***remember to not add a stylesheet directly to the FXML***

Modifying the visuals can be done via the FXML and SceneBuilder, and modifying actions on page can be done via the controller as per usual
