import Route from '@ember/routing/route';

export default Route.extend({
  model(){
    return Ember.RSVP.hash({
      categoryList: this.store.findAll('category'),
      products: this.store.queryRecord('product', { name: '', category: '', favorite: '', releaseDate: '', expiryDate: '', rating: '' }).then(),
      productRandom: this.store.findRecord('product', 1)
    })
  }
});
