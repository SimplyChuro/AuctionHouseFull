import DS from 'ember-data';

export default DS.Model.extend({
  authToken: DS.attr('string'),
  name: DS.attr('string'),
  surname: DS.attr('string'),
  email: DS.attr('string'),
  password: DS.attr('string'),
  emailVerified: DS.attr('boolean'),
  avatar: DS.attr('string'),
  gender: DS.attr('string'),
  dateOfBirth: DS.attr('date'),
  phoneNumber: DS.attr('string'),
  phoneVerified: DS.attr('boolean'),
  bids: DS.hasMany('bid')
});
