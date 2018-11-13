import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  publishDate: DS.attr('date'),
  expireDate: DS.attr('date'),
  mainBid: DS.attr('number'),
  status: DS.attr('string'),
  color: DS.attr('string'),
  size: DS.attr('string'),
  description: DS.attr('string'),
  startingPrice: DS.attr('number'),
  pictures: DS.hasMany('picture'),
  bids: DS.hasMany('bid')
});
