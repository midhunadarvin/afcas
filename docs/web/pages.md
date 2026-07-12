# Web Pages & Routes

| Path | Controller | Description |
|------|------------|-------------|
| `/` | `HomeController` | Dashboard |
| `/principals` | `PrincipalsController` | List principals |
| `/principals/new` | | Create form |
| `POST /principals` | | Create principal |
| `POST /principals/{id}/delete` | | Delete |
| `/principals/{id}/members` | | Group members |
| `POST /principals/{groupId}/members` | | Add member |
| `POST /principals/{groupId}/members/{memberId}/remove` | | Remove member |
| `/operations` | `OperationsController` | List/create/delete operations |
| `/resources` | `ResourcesController` | List/create/delete resources |
| `/permissions` | `PermissionsController` | List/add/remove grants |
| `/authorization` | `AuthorizationController` | Check authorization |
| `POST /authorization/check` | | Submit check |
| `/authorization/digest/{principalId}` | | Digest view |
| `POST /authorization/authorized-operations` | | Ops query |
| `POST /authorization/authorized-resources` | | Resources query |

## Templates

Located under `app/src/main/resources/templates/`:

- `layout.html`, `index.html`
- `principals.html`, `principal-form.html`, `group-members.html`
- `operations.html`, `operation-form.html`
- `resources.html`, `resource-form.html`
- `permissions.html`, `permission-form.html`
- `authorization.html`, `authorization-digest.html`
- `authorized-operations.html`, `authorized-resources.html`
