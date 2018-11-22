import DS from 'ember-data';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({
  name: validator('presence', true),
  surname: validator('presence', true),
  email: [ 
    validator('presence', true),
    validator('format', { type: 'email' })
  ],
  password: [ 
    validator('presence', true),
    validator('length', { 
      min: 4 
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
  bids: DS.hasMany('bid')
});
