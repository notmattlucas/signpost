/**
 * Writes message to messages collection
 */
exports = function(json){
  let messages = context.services.get("mongodb-atlas").db("signpost").collection("messages");
  let message = JSON.parse(json);
  let location = message.location;
  let result = messages.insertOne({
    _id: new BSON.ObjectId(message.id),
    user: message.user,
    message: message.message,
    location: {
      type: 'Point',
      coordinates: [location.longitude, location.latitude]
    }
  })
  return true;
};
