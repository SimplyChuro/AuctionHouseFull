import DS from 'ember-data';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({


  user: validator('belongs-to'),

  // street: validator('presence', {
  //   presence: true,
  //   ignoreBlank: true,
  //   message: "Street should not be empty"
  // }),

  // city: validator('presence', {
  //   presence: false,
  //   ignoreBlank: true,
  //   message: "City should not be empty"
  // }),

  // zipCode: validator('presence', {
  //   presence: false,
  //   ignoreBlank: true,
  //   message: "ZipCode should not be empty"
  // }),

  // state: validator('presence', {
  //   presence: false,
  //   ignoreBlank: true,
  //   message: "State should not be empty"
  // }),

  // country: validator('presence', {
  //   presence: false,
  //   ignoreBlank: true,
  //   message: "Country should not be empty"
  // }),

});

export default DS.Model.extend(Validations, {
  street: DS.attr('string'),
  city: DS.attr('string'),
  zipCode: DS.attr('string'),
  state: DS.attr('string'),
  country: DS.attr('string'),
  user: DS.belongsTo('user')
});
