import Route from '@ember/routing/route';

export default Route.extend({
  model(){
    return Ember.RSVP.hash({
      productList: this.store.query('product', { name: '', category: 0, featured: '', status: '', rating: '' })
    })
  }
});
