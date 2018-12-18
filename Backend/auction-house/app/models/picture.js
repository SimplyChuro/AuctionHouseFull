import DS from 'ember-data';

export default DS.Model.extend({
  url: DS.attr('string'),
  product: DS.belongsTo('product')
});
