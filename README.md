# emoncms Persistence service for openHAB

This binding provides a very basic persistence service for emoncms (http://emoncms.org/)

## Installation

Compile with Maven then drop the .jar file  folder in your openHAB `addons` directory.

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called emoncms.persist in the `${openhab.home}/configuration/persistence` folder.

## Configuration

This persistence service can be configured in `openhab.cfg`.
The configuration should look like this : 

>######################## Emoncms Persistence Service ##############################
>\#
>\# the url of the emoncms server (optional, default is http://emoncms.org/)

> emoncms:url=

>\# the emoncms API-Key for authentication (mandatory, generated on the emoncms server or website)

> emoncms:apikey=

>\# the node number (optional, default is 0)

> emoncms:node=

>\# if the value should be rounded (optional, default is false)

> emoncms:round=


All item and event related configuration is done in the emoncms.persist file. Aliases do not have any special meaning for the emoncms persistence service.



## Development

This is a very simple dev, for a very basic use. It only posts datas as inputs on the server.
Feeds and logs need to be created elsewhere.
