import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
  attrs: {
    pictures: { embedded: 'always' },
    productcategory: { embedded: 'always' }
  }, 
  normalizeResponse(store, primaryModelClass, payload, id, requestType){
    payload = {product:payload};
    return this._super(store, primaryModelClass, payload, id, requestType);
  }
});