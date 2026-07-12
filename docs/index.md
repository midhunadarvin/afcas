---
layout: home

hero:
  name: AFCAS
  text: Authorization Sub-System
  tagline: A fairly capable, hierarchical RBAC engine backed by PostgreSQL — with CLI, Web UI, and dual engine modes.
  actions:
    - theme: brand
      text: Get Started
      link: /guide/introduction
    - theme: alt
      text: Architecture
      link: /architecture/overview
    - theme: alt
      text: CLI Reference
      link: /cli/overview

features:
  - title: Hierarchical Authorization
    details: Principals (users/groups), operations, and resources form graphs. Permissions inherit through transitive closure for O(1) auth checks.
  - title: Dual Interfaces
    details: Interactive PicoCLI shell for scripting and automation, plus a Spring Boot + Thymeleaf web UI for day-to-day management.
  - title: SQL or Java Engine
    details: Run authorization logic in PostgreSQL stored procedures (classic) or in the portable Java application layer via afcas.engine.mode.
  - title: PostgreSQL-Native
    details: Pre-computed Edge transitive closure, stored procedures for CRUD and queries, and optional auto-schema initialization on web startup.
---
