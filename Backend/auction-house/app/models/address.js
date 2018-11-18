import DS from 'ember-data';

export default DS.Model.extend({
  street: DS.attr('string'),
  city: DS.attr('string'),
  zipCode: DS.attr('string'),
  state: DS.attr('string'),
  country: DS.attr('string'),
  user: DS.belongsTo('user')
});
