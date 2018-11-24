import DS from 'ember-data';
import { buildValidations, validator } from 'ember-cp-validations';

const Validations = buildValidations({
  amount: [ 
    validator('presence', true),
 
    validator('number', {
      allowString: true,
      float: true,
      gt: 0
    })
  ]
});

export default DS.Model.extend(Validations, {
  amount: DS.attr('number'),
  date: DS.attr('date'),
  status: DS.attr('string'),
  product_id: DS.attr('number'),
  user: DS.belongsTo('user'),
  product: DS.belongsTo('product')
});
