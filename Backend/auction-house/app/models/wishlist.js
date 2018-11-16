import DS from 'ember-data';

export default DS.Model.extend({
  status: DS.attr('string'),
  product_id: DS.attr('number'),
  user: DS.belongsTo('user'),
  product: DS.belongsTo('product')
});
