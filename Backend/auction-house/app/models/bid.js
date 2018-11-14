import DS from 'ember-data';

export default DS.Model.extend({
  amount: DS.attr('number'),
  date: DS.attr('date'),
  status: DS.attr('string'),
  product_id: DS.attr('number'),
  user: DS.belongsTo('user'),
  product: DS.belongsTo('product')
});
