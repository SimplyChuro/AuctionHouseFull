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
  duration: Ember.computed('publishDate', 'expireDate', function() {
    var date1 = new Date(this.get('publishDate'));
    var date2 = new Date(this.get('expireDate'));
    var timeDiff = Math.abs(date2.getTime() - date1.getTime());
    var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
    return diffDays;
  }),
  startingPriceFormated: Ember.computed('startingPrice', function() {
    var price = this.get('startingPrice'),
    formatted = parseFloat(price, 10).toFixed(2);

    return '$' + formatted;
  }),
  mainBidFormated: Ember.computed('mainBid', function() {
    var price = this.get('mainBid'),
    formatted = parseFloat(price, 10).toFixed(2);

    return '$' + formatted;
  }),

});
