/**
 * Searchs for messages matching the given term
 */ 
exports = function(term){
  let messages = context.services.get("mongodb-atlas").db("signpost").collection("messages");
  let records = messages.aggregate([{
    $search: {
      index: 'messages',
      text: {
        query: term,
        path: {
          'wildcard': '*'
        }
      }
    }
  }, { 
    $limit: 100 
  }]).toArray();
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
