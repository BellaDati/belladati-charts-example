# BellaDati Charts.js in Java application
This repository contains Java project dedicated to render SVG charts from BellaDati JSON data in stand-alone Java application.

Refer to [Integrate to Java application](http://support.belladati.com/techdoc/Integrate+to+Java+application) for more details.

## Application scope

This stand-alone Java application allows you to:

1. Render SVG charts from JSON data
2. Customize rendered JSON data
3. Cooperate with BellaDati through our [SDK](https://github.com/BellaDati/belladati-sdk-java):
  * login/logout
  * get list of reports
  * get list of views
  * get available attributes
  * apply filter based on attribute value
  * get chart JSON data

## Usage

How to run this application in Eclipse IDE:

 1. Clone this repository to your localhost (you can also download it as ZIP)
 2. Open Eclipse IDE and import project (File -> Import -> Maven -> Existing Maven project)
 3. Select folder where you cloned/unzipped repository (folder should contain pom.xml file)
 4. Select this project in the list and follow instructions in import wizard
 5. Select project in navigator/explorer view, right click and update Maven configuration (Maven -> Update Project)
 6. Run Main.java class as simple standalone Java application (Run as -> Java Application)

Notes:

* You need JDK 1.8+ to run this application.

## Tutorial

![Main Window](/tutorial/main-window.PNG)

#### Render chart from sample JSON data

Simply click on button "Sample chart". You will see sample chart.

#### Customizing rendered JSON data

Click on button "Custom JSON data" and change appropriate property in displayed JSON based on your preferences. Then click on button "Render view".

![Customizing rendered JSON data](/tutorial/customize-json-window.PNG)

#### Cooperating with BellaDati

Click on button "Chart from server". Then enter connection parameters to BellaDati instance and credentials into 4 input boxes located on the left side of displayed window. Then click "Login".

![Cooperating with BellaDati](/tutorial/sdk-window.PNG)

Select report and view that you want to render and click on button "Render view". Main window will be refreshed and filter toolbar will be displayed on the right side.

![Filtering](/tutorial/filter-toolbar.PNG)

You can select attribute that you want to filter by. Values will be refreshed and you can select one or more of them. To apply filter click on button "Render view".

#### Clear chart JSON data

Simply click on button "Clear".

#### Quit application

Simply click on button "Quit".