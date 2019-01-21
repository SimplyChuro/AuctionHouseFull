import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
  attrs: {
    reviews: { embedded: 'always' },
    wishlist: { embedded: 'always' },
    sales: { embedded: 'always' },
    address: { embedded: 'always' }
  }, 
  normalizeResponse(store, primaryModelClass, payload, id, requestType){
    payload = {user:payload};
    return this._super(store, primaryModelClass, payload, id, requestType);
  }
});