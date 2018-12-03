import Route from '@ember/routing/route';

export default Route.extend({
  model(){
    return Ember.RSVP.hash({
      categoryList: this.store.findAll('category'),
      productList: this.store.findAll('product'),
      productRandom: this.store.findAll('product').then((list) => {
        const rand = Math.floor(Math.random() * list.get('length'));
        return list.objectAt(rand);
      })
    })
  },

  actions: {
    refresh: function() {
      this.refresh();
    },
    
    willTransition: function(transition) {
      this.controllerFor('shop/product-list').send('clearFields');
    }
  }
});
