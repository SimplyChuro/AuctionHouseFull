import DS from 'ember-data';
import { mapBy } from '@ember/object/computed';
import { sum } from '@ember/object/computed';
import { computed } from '@ember/object';

export default DS.Model.extend({
  name: DS.attr('string'),
  publishDate: DS.attr('date'),
  expireDate: DS.attr('date'),
  startingPrice: DS.attr('number'),
  status: DS.attr('string'),
  color: DS.attr('string'),
  size: DS.attr('string'),
  description: DS.attr('string'),
  featured: DS.attr('boolean'),
  category_id: DS.attr('number'),
  pictures: DS.hasMany('picture'),
  productcategory: DS.hasMany('productcategory'),
  sale: DS.belongsTo('sale'),
  wishlist: DS.hasMany('wishlist'),
  bids: DS.hasMany('bid'),
  reviews: DS.hasMany('review'),

  ratingScores: mapBy('review', 'rating'),
  sumOfScores: sum('ratingScores'),
  averageScore: computed('sumOfScores', 'ratingScores.length', function() {
    return this.get('sumOfScores') / this.get('ratingScores.length');
  })
});
