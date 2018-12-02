import DS from 'ember-data';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({
  
  address: validator('presence', {
    presence: true,
    ignoreBlank: true,
    message: "Address should not be empty"
  }),

  city: validator('presence', {
    presence: true,
    ignoreBlank: true,
    message: "City should not be empty"
  }),

  zipCode: validator('presence', {
    presence: true,
    ignoreBlank: true,
    message: "ZipCode should not be empty"
  }),

  phone: [
    validator('presence', {
      presence: true,
      ignoreBlank: true,
      message: "Phone should not be empty"
    }),
    
    validator('format', { 
      allowBlank: false,
      type: 'phone'
    })
  ],

  country: validator('presence', {
    presence: true,
    ignoreBlank: true,
    message: "Country should not be empty"
  }),

});

export default DS.Model.extend(Validations, {
  address: DS.attr('string'),
  city: DS.attr('string'),
  zipCode: DS.attr('string'),
  phone: DS.attr('string'),
  country: DS.attr('string'),
  status: DS.attr('string'),
  product_id: DS.attr('number'),
  user: DS.belongsTo('user'),
  product: DS.belongsTo('product')
});
