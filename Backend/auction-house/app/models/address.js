import DS from 'ember-data';

export default DS.Model.extend({
  street: DS.attr('street'),
  city: DS.attr('city'),
  zipCode: DS.attr('zipCode'),
  state: DS.attr('state'),
  country: DS.attr('country'),
  user: DS.belongsTo('user'),
});
