/**
 * Finds all messages within a 10km distance from a given latitude / longitude
 **/
exports = (lat, long) => {
  let messages = context.services.get("mongodb-atlas").db("signpost").collection("messages");
  let records = messages.find({
   location: {
     $near: {
       $geometry: {
          type: "Point" ,
          coordinates: [ long, lat ]
       },
       $maxDistance: 10000,
       $minDistance: 0
     }
   }
  }).toArray();
  records = records.map(record => {
    let [long, lat] = record.location.coordinates;
    return {
      id: record._id.toString(),
      message: record.message,
      user: record.user,
      location: {
        latitude: lat,
        longitude: long
      }
    }
  });
  return records;
};
