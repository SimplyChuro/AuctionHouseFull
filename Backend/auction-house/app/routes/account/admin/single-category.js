import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(params) {
    return hash({
      category: this.store.findRecord('category', params.category_id, { reload: true }),
      categoryList: this.store.findAll('category')
    })
  },
  
  setupController(controller, model) {
    this._super(controller, model);
    this.controllerFor('account/admin/single-category').set('name', model.category.name);
  },

  actions: { 
    willTransition: function() {
      this.controllerFor('account/admin/single-category').send('clearFields');
    }
  }
});

