import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  parent_id: DS.attr('number'),
  productcategory: DS.hasMany('productcategory'),
});
