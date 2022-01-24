# candlestick Challenge

Your task is to build a system that enables users to view price histories. 
It will receive updates from a partner service, transform these updates and provide the aggregated data through an endpoint.

## Content
- Intro and terminology
- Setup
- Future Development Discussion

## Intro and terminology

#### Instruments and Quotes
Every asset that can be traded is represented by an “instrument”, which has a unique identifier (ISIN).
Each time the instrument price changes, an update message called “quote” is broadcasted for this instrument to inform about the change. 

#### What is a candlestick? 
A [candlestick](https://en.wikipedia.org/wiki/Candlestick_chart) is a representation that describes the price movement for a given instrument in a fixed amount of time, usually one minute.
We will be using a simplified version of candlesticks for this challenge.

![chart](https://t4.ftcdn.net/jpg/02/79/65/79/360_F_279657943_FALhJZ6g4shXyfqMIRifp1l6lhiwhbwm.jpg)

The basic idea is that we don't need to know about _all_ prices changes within a given timeframe.
Usually we want them grouped in 1 minute chunks, because we are more interested in some key data points within any given minute.
In theory, a candlestick “contains” all quotes, where the timestamp of the quote is higher than the openTimestamp and lower than the closeTimestamp (`openTimestamp <= quoteTimestamp < closeTimestamp`).
However, for each candle for each given minute, we only present the following data points to the user:
- the first quotes price, that was received (openPrice)
- the last quotes, that was received (closePrice) 
- the highest quote price that was observed (highPrice)
- the lowest quote price that was observed (lowPrice)
- the timestamp when the candlestick was opened (openTimestamp)
- the timestamp when the candlestick was closed (closeTimestamp)

##### Example
Assume the following (simplified) data was received for an instrument:
```
@2019-03-05 13:00:05 price: 10
@2019-03-05 13:00:06 price: 11
@2019-03-05 13:00:13 price: 15
@2019-03-05 13:00:19 price: 11
@2019-03-05 13:00:32 price: 13
@2019-03-05 13:00:49 price: 12
@2019-03-05 13:00:57 price: 12
@2019-03-05 13:01:00 price: 9
```
The resulting minute candle would have these attributes:
```
openTimestamp: 2019-03-05 13:00:00
openPrice: 10
highPrice: 15
lowPrice: 10
closePrice: 12
closeTimestamp: 13:01:00
```
Note that the last received quote with a price of 9 is not part of this candlestick anymore, but belongs to the new candlestick.

### Input data
The input data is received through a websocket stream from a partner service.
The code provides handles connecting and consuming the stream (`Streams.kt`).
There are two types of input messages:

  - Instrument additions/deletions, which adds or removes an instrument from our catalogue
  - Instrument price updates, giving the most recent price for a specific instrument


### Output (Aggregated-Price History)
The output (and the main task of this challenge) is the aggregated price history endpoint.
It should provide a 30 minutes quotes history in the form of minute candlesticks (check information below) for any requested instrument.

End-users of the service are interested in a specific instruments price history, and they want it in a format that is easy to read.
Hence we should provide candlesticks.
To get these candlesticks, the user needs to provide the instrument id (ISIN) as a query parameter (e.g. `http://localhost:9000/candlesticks?isin={ISIN}`). 

The system needs to return only the candlesticks for the last 30 minutes.
If there weren't any quotes received for more than a minute, instead of missing candlesticks in the 30 minute window, values from the previous candle are reused.

The endpoint stub for fetching candlesticks is already provided in `Server.kt`. Fully implementing the body of that method is the main task.

**Your task is to implement a service that will consume the stream data and make it reachable through the API in the format mentioned above**

### Open questions

If you feel that the requirements leave you with open questions - that is on purpose and part of the challenge.
You are free to make assumptions or reach out to us, when you really need feedback.
We highly appreciate if you document the assumptions you made. We will probably ask you to argument those assumptions in the next interview steps.

# Setup 

You can either choose to use the framework we provide you, or to use your own framework.
For the latter, see the specification information about the partner service at the end of this section.

## Framework Requirements
  - JVM running on your local machine
  - Gradle
  - Docker v2
  - An IDE of your choice

### Running the Partner Service

To run a partner service you can either use docker-compose. Docker v3 or above will require slight changes to the docker-compose files.
``` 
docker-compose up -d
```
or Java
```
java -jar partner-service-1.0.1-all.jar --port=8032
```

### Running the app
To run the app you can use the following gradle commands  
```
./gradlew build
./gradlew test
./gradlew run
```

Once the server is running you can check the results at
```
http://localhost:9000/candlesticks?isin={ISIN}
```
Note: If you don't implement your service, you will see an exception here

### Using your own Framework

You are free to do the challenge without the code we provided.
Most likely you will need the following information to do so.

#### PartnerService
This coding challenge includes a runnable JAR (the partner service) that provides the websockets mentioned below.
If you decide to not use the given boilerplate code, you will need the following information.
Running the jar in a terminal with `-h` or `--help` will print how to use it.
The default port is `8080`, but you are free to change that.
We like `8032` and use that for the following examples.

Once started, it provides two websocket streams (`ws://localhost:8032`), plus a website preview of how the stream look (http://localhost:8032).

* `/instruments` provides the currently available instruments with their ISIN and a Description

    * when connecting to the stream, it gives all currently available Instruments
    * once connected it streams the addition/removal of instruments
    * Our partners assured us, that ISINS are unique, but can in rare cases be reused once no Instrument with that ISIN is available anymore (has been deleted, etc.)

* `/quotes` provides the most current price for an instrument every few seconds per available instrument

If you restart the PartnerService, you will have to clean up any data you might have persisted, since it will generate new ISINs and does not retain state from any previous runs.

##### /instrument Specification
The `/instruments` websocket provides all currently active instruments `onConnect`, as well as a stream of add/delete events of instruments.
When you receive a `DELETE` event, the instrument is no longer available and will not receive any more quotes (beware out of order messages on the /quotes stream)
The instruments are uniquely identified by their isin. Beware, ISINs can be reused _after_ an instrument has been deleted.
In any case, you would see a regular ADD event for this new instrument, even when it reuses an ISIN.

```
{
    // The type of the event. ADD if an instrument is ADDED
    // DELETE if an instrument is deleted
    "type": "DELETE"
    {
        //The Payload
        "data": {
            //The Description of the instrument
            "description": "elementum eos accumsan orci constituto antiopam",
            //The ISIN of this instrument
            "isin": "LS342I184454"
        }
    }
}
```

##### /quotes SpecificationRunni
The `/quotes` Websocket provides prices for available instruments at an arbitrary rate.
It only streams prices for available instruments (beware out of order messages).
```
{
    // The type of the event.
    // QUOTE if an new price is available for an instrument identified by the ISIN
    "type": "QUOTE"
    {
        //The Payload
        "data": {
            //The price of the instrument with arbitray precision
            "price": 1365.25,
            //The ISIN of this instrument
            "isin": "LS342I184454"
        }
    }
}
```

## Future Development Discussion
The following questions will give you a hint on what to think about for the code review interview.

- How would you change the system to provide scaling capabilities to 50.000 (or more) available instruments, each streaming quotes between once per second and every few seconds?
- How could this system be build in a way that supports failover capabilities so that multiple instances of the system could run simultaneously?
