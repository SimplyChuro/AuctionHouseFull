import DS from 'ember-data';

export default DS.Model.extend({
  url: DS.attr('string'),
  main: DS.attr('boolean'),
  product: DS.belongsTo('product')
});
