import DS from 'ember-data';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({

  address: validator('belongs-to'),

  name: validator('presence', {
    presence: true,
    ignoreBlank: true,
    message: "First Name should not be empty"
  }),

  surname: validator('presence', {
    presence: true,
    ignoreBlank: true,
    message: "Last Name should not be empty"
  }),

  email: [ 
    validator('presence', true),
    validator('format', { type: 'email' })
  ],

  password: [ 
    validator('presence', true),
    validator('length', { 
      min: 4 
    }),

    validator('format', {
      regex: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{4,64}$/,
      message: 'Password must include at least one upper case letter, one lower case letter, and a number'
    })
  ],

  passwordConfirmation: validator('confirmation', {
    on: 'password',
    message: 'Passwords do not match'
  }),

  emailConfirmation: validator('confirmation', {
    on: 'email',
    message: 'Email addresses do not match'
  }),

  phoneNumber: [ 
    validator('format', { 
      allowBlank: true,
      regex: /^\+{0,1}[0-9, ]*$/,
      message: 'Phone number can not contain alphabet letters'
    })
  ]

});

export default DS.Model.extend(Validations, {
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
  address: DS.belongsTo('address'),
  wishlist: DS.hasMany('wishlist'),
  bids: DS.hasMany('bid'),
  sales: DS.hasMany('sale')
});
