import DS from 'ember-data';

export default DS.Model.extend({
  address: DS.attr('string'),
  city: DS.attr('string'),
  zipCode: DS.attr('string'),
  phone: DS.attr('string'),
  country: DS.attr('string'),
  status: DS.attr('string'),
  product_id: DS.attr('number'),
  user: DS.belongsTo('user'),
  product: DS.belongsTo('product')
});
