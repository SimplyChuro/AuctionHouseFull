import { helper } from '@ember/component/helper';

export function isActive(params) {
  const index = params[0];
  const active = params[1];
  const mode = params[2];
  if(mode === 0){
    return (index === active) ? 'category-table-link-v2 category-table-link-active': 'category-table-link-v2';
  }else if(mode === 1) {
    return (index === active) ? 'category-table-link-v3 category-table-link-active': 'category-table-link-v3';
  }else if(mode === 2) {
    return (index === active) ? 'product-list-button product-list-button-active': 'product-list-button';
  }
}

export default helper(isActive);
