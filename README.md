# t2-rest-leds-mockup
A mockup server that simulates a REST interface to two LEDs of a Tessel2 and displays the board on the screen.

## Run
````
$ mvn jetty:run
````

## Use

````
$ curl localhost:8080
````
and follow the links. FWIW, you can overwrite the LEDs' state.

---
### License
Images are based on https://github.com/tessel/t2-fritzing and are hence under [CC-BY-SA 4.0 license](http://creativecommons.org/licenses/by-sa/4.0/).
Code is under MIT license.
