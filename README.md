# Signpost

For those of us who have had the good sense and fortune (or possibly misfortune?) to play one of the Dark Souls series, we're intimately familiar with the mystical orange soapstone. This item allows players to drop messages anywhere within their world that will then appear to other players in their own worlds. These are used to hint, warn, amuse or (more often than not troll) other players as they journey through the treacherous land of Lordran.

### Overview of My Submission

![SignPost](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/2aqidzw5kcth10r79s9b.png)

I took inspiration from the idea of the orange soapstone - specifically being able to drop messages at any location for others to read - and came up with the idea of 'SignPost', a geo-social network. This is a platform that allows you to drop messages (signposts) anywhere you like and to read the messages other people have left, where you're located. This could be great for directions, sports (cross-country hiking), games, etc - but is mostly just a fun idea for a hackathon.

SignPost is an Android app supported exclusively by the MongoDB Atlas platform, from user-authentication and login through to geo-querying and search. The app has four core features:
* Signup and authentication using your Google credentials (via MongoDB Realm).
* Browsing a map marked with "signposts" - messages dropped by other users near your location.
* Dropping a signpost yourself.
* Searching for signposts across the world 

#### 1. A map filled with signposts

Once logged in (via your Google credentials) you're presented with a map zoomed in on your current location. The app fetches any messages in your local area and plots them on the map for you to interact with. These appear as orange markers, and will display the message when touched.

{% youtube RDmtvayeyg0 %}

#### 2. Dropping your own signpost on the map

If you have something to say you can easily post your own message on the map. Just tap the exact point at which you want to post it and type away.

{% youtube 77qtHJAXCio %}

#### 3. Searching for signposts across the world

You can search for messages across the whole world, thanks to the Atlas search integration. Simply click the search icon (top right) pick the message from your search results and your map will be immediately transported around the world.

{% youtube TD9oVm_x9vY %}

#### How does it all hang together?

The SignPost app is backed completely by the MongoDB Atlas platform, and uses a number of its features:

* **Realm / Google Authentication** to seamlessly add google user signon and tracking to the application.

* **MongoDB Atlas with Geospatial Indexes** to allow the plotting of messages with their latitude/longitude co-ordinates, and to query records within the locality of a point.

* **Atlas Search** to make it trivial to map the signpost messages into a free text search.

* **Realm Functions** to provide a serverless set of backend APIs for the three key functions of the app - reading, writing and searching for messages.
 

![Architecture](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/1ejhty2yb4qps3t17civ.jpg)


In summary, MongoDB Atlas was able to provide a complete back-end stack (with the exception of Google Maps) to develop a fully functional Android application, even though I'd never written a line of Android code before :astonished:

[Note]: # (Please make sure the project links to the appropriate GitHub repository, and includes the [Apache-2 permissive license](https://www.apache.org/licenses/LICENSE-2.0)  and README.)


### Submission Category: 

Choose Your Own Adventure


### Link to Code

{% github https://github.com/lucas-matt/signpost %}


### Additional Resources / Info

* [Geospatial Queries](https://docs.mongodb.com/manual/geospatial-queries/)
* [Google Authentication](https://docs.mongodb.com/realm/authentication/google/)
* [Geospatial Queries](https://docs.mongodb.com/manual/geospatial-queries/)
* [Atlas Search](https://docs.atlas.mongodb.com/atlas-search/)
* [Realm Functions](https://docs.mongodb.com/realm/functions/)
