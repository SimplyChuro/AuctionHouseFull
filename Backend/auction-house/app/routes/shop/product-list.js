import Route from '@ember/routing/route';

export default Route.extend({
  model(){
    return Ember.RSVP.hash({
      categoryList: this.store.findAll('category')
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
