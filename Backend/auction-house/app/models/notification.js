import DS from 'ember-data';

export default DS.Model.extend({
  message: DS.attr('string'),
  created: DS.belongsTo('date'),
  expiresAt: DS.belongsTo('date')
});
