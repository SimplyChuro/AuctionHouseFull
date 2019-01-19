import Helper from '@ember/component/helper';

export default Helper.extend({
  
  compute(params) {
    let product = params[0];
    var type = params[1];
    var product_category = null;
    
    product.get('productcategory').forEach((item) => {
      if(type == 'parent'){
        if(item.category.get('parent_id') == null){
          product_category = item.get('category').get('id');
        }
      } else {
        if(item.category.get('parent_id') != null){
          product_category = item.get('category').get('id');
        }
      }
    });

    return product_category;
  }
  
});
