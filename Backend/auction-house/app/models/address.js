import DS from 'ember-data';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({

  user: validator('belongs-to')
  
});

export default DS.Model.extend(Validations, {
  street: DS.attr('string'),
  city: DS.attr('string'),
  zipCode: DS.attr('string'),
  state: DS.attr('string'),
  country: DS.attr('string'),
  user: DS.belongsTo('user')
});
