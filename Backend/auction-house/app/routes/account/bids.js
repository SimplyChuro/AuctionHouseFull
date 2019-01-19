import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model() {
    return hash({
      productList: this.store.query('product', { name: '', category: 0, featured: '', status: '', rating: '' })
    })
  }
});
