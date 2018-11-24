import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
  attrs: {
    product: { embedded: 'always' },
    user: { embedded: 'always' }
  },
  normalizeResponse(store, primaryModelClass, payload, id, requestType){
    payload = {bid:payload};
    return this._super(store, primaryModelClass, payload, id, requestType);
  }
});
