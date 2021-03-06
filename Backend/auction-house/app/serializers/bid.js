import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
  attrs: {
    user: { embedded: 'always' }
  },
  normalizeResponse(store, primaryModelClass, payload, id, requestType){
    payload = {bid:payload};
    return this._super(store, primaryModelClass, payload, id, requestType);
  }
});
