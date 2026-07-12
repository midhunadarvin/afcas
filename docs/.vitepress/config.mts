import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'AFCAS',
  description: 'A Fairly Capable Authorization Sub-System — documentation',
  cleanUrls: true,
  ignoreDeadLinks: true,
  themeConfig: {
    logo: undefined,
    siteTitle: 'AFCAS Docs',
    nav: [
      { text: 'Guide', link: '/guide/introduction' },
      { text: 'Concepts', link: '/concepts/authorization-model' },
      { text: 'Architecture', link: '/architecture/overview' },
      { text: 'CLI', link: '/cli/overview' },
      { text: 'Web UI', link: '/web/overview' },
      { text: 'Development', link: '/development/project-structure' },
      { text: 'Reference', link: '/reference/configuration' }
    ],
    sidebar: {
      '/guide/': [
        {
          text: 'Getting Started',
          items: [
            { text: 'Introduction', link: '/guide/introduction' },
            { text: 'Prerequisites', link: '/guide/prerequisites' },
            { text: 'Quick Start', link: '/guide/quick-start' },
            { text: 'Database Setup', link: '/guide/database-setup' },
            { text: 'Building', link: '/guide/building' },
            { text: 'Configuration', link: '/guide/configuration' },
            { text: 'Docker', link: '/guide/docker' }
          ]
        }
      ],
      '/concepts/': [
        {
          text: 'Core Concepts',
          items: [
            { text: 'Authorization Model', link: '/concepts/authorization-model' },
            { text: 'Principals', link: '/concepts/principals' },
            { text: 'Operations', link: '/concepts/operations' },
            { text: 'Resources', link: '/concepts/resources' },
            { text: 'Access Predicates', link: '/concepts/access-predicates' },
            { text: 'Hierarchy & Edges', link: '/concepts/hierarchy-and-edges' },
            { text: 'Authorization Decision', link: '/concepts/authorization-decision' }
          ]
        }
      ],
      '/architecture/': [
        {
          text: 'Architecture',
          items: [
            { text: 'System Overview', link: '/architecture/overview' },
            { text: 'Database Schema', link: '/architecture/database-schema' },
            { text: 'Java Layer', link: '/architecture/java-layer' },
            { text: 'Engine Modes (SQL vs Java)', link: '/architecture/engine-modes' },
            { text: 'Stored Procedures', link: '/architecture/stored-procedures' }
          ]
        }
      ],
      '/cli/': [
        {
          text: 'CLI Reference',
          items: [
            { text: 'Overview', link: '/cli/overview' },
            { text: 'Connection', link: '/cli/connection' },
            { text: 'Principals', link: '/cli/principals' },
            { text: 'Operations', link: '/cli/operations' },
            { text: 'Resources', link: '/cli/resources' },
            { text: 'Permissions', link: '/cli/permissions' },
            { text: 'Hierarchy', link: '/cli/hierarchy' },
            { text: 'Queries & Checks', link: '/cli/queries' },
            { text: 'Full Command Cheat Sheet', link: '/cli/cheat-sheet' }
          ]
        }
      ],
      '/web/': [
        {
          text: 'Web Application',
          items: [
            { text: 'Overview', link: '/web/overview' },
            { text: 'Pages & Routes', link: '/web/pages' },
            { text: 'Running the Web UI', link: '/web/running' }
          ]
        }
      ],
      '/development/': [
        {
          text: 'Development',
          items: [
            { text: 'Project Structure', link: '/development/project-structure' },
            { text: 'Testing', link: '/development/testing' },
            { text: 'Native Image (GraalVM)', link: '/development/native-image' },
            { text: 'Troubleshooting', link: '/development/troubleshooting' }
          ]
        }
      ],
      '/reference/': [
        {
          text: 'Reference',
          items: [
            { text: 'Configuration Properties', link: '/reference/configuration' },
            { text: 'SQL Scripts', link: '/reference/sql-scripts' },
            { text: 'Gradle Tasks', link: '/reference/gradle-tasks' },
            { text: 'Domain Model', link: '/reference/domain-model' }
          ]
        }
      ]
    },
    socialLinks: [],
    search: {
      provider: 'local'
    },
    footer: {
      message: 'A Fairly Capable Authorization Sub-System',
      copyright: 'See LICENSE in the repository root'
    },
    outline: {
      level: [2, 3]
    },
    editLink: false,
    lastUpdated: false
  },
  head: [
    ['meta', { name: 'theme-color', content: '#3e63dd' }]
  ]
})
