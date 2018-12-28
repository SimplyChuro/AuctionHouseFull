import DS from 'ember-data';

export default DS.Model.extend({
  url: DS.attr('string'),
  pictureFile: DS.attr('file'),
  product: DS.belongsTo('product')
});
