import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  publishDate: DS.attr('date'),
  expireDate: DS.attr('date'),
  mainBid: DS.attr('number'),
  bidCount: DS.attr('number'),
  startingPrice: DS.attr('number'),
  status: DS.attr('string'),
  color: DS.attr('string'),
  size: DS.attr('string'),
  description: DS.attr('string'),
  pictures: DS.hasMany('picture'),
  productcategory: DS.hasMany('productcategory'),
  wishlist: DS.hasMany('wishlist'),
  bids: DS.hasMany('bid')
});
